package ru.mail.polis.maxciv.cluster;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.data.Replicas;
import ru.mail.polis.maxciv.util.CommonUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.mail.polis.maxciv.util.ResponceUtils.ACCEPTED;
import static ru.mail.polis.maxciv.util.ResponceUtils.BAD_REQUEST;
import static ru.mail.polis.maxciv.util.ResponceUtils.CREATED;
import static ru.mail.polis.maxciv.util.ResponceUtils.GATEWAY_TIMEOUT;
import static ru.mail.polis.maxciv.util.ResponceUtils.NOT_FOUND;
import static ru.mail.polis.maxciv.util.ResponceUtils.OK;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_ACCEPTED;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_CREATED;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_NOT_FOUND;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_OK;

public class ClusterService {

    public static final int METHOD_GET     = 1;
    public static final int METHOD_PUT     = 5;
    public static final int METHOD_DELETE  = 6;

    public static final String REPLICATION_HEADER = "Replication: ";

    private static final String REPLICATION_REQUEST_URL = "/v0/entity?replicas=3/3&id=";
//    private static final String REPLICATION_REQUEST_URL = "/v0/entity?id=";

    private final StorageService storageService;
    private final int currentPort;
    private final Set<String> topology;
    private final List<Node> clusterNodes;

    public ClusterService(int port, KVDao dao, Set<String> topology) {
        this.storageService = new StorageService(dao);
        this.currentPort = port;
        this.topology = topology;
        this.clusterNodes = topology.stream().map(Node::new).collect(Collectors.toList());
    }

    public Response getObject(String key, String replicasString, boolean isReplication) {
        Replicas replicas = getReplicasFromString(replicasString);
        if (replicas == null) {
            return BAD_REQUEST();
        }

        if (isReplication) {
            return storageService.getObject(key);
        }

        int ackCount = 0;
        boolean removedFlag = false;
        byte[] resultValue = null;
        long newerTimestamp = 0;
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));

        for (int i = 0; i < replicas.getFrom(); i++) {
            Response response;

            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.getObject(key);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), METHOD_GET, key, null);
            }

            if (response != null) {
                if (response.code() == STATUS_OK || response.code() == STATUS_NOT_FOUND) {
                    ackCount++;
                    if (response.code() == STATUS_OK) {
                        String timestamp = response.header(StorageService.ENTITY_TIMESTAMP_HEADER);
                        Long objectTimestamp = new Long(timestamp);

                        if (response.header(StorageService.ENTITY_REMOVED_HEADER) != null) {
                            removedFlag = true;
                        } else if (objectTimestamp > newerTimestamp) {
                            newerTimestamp = objectTimestamp;
                            try {
                                resultValue = response.body().bytes();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (ackCount >= replicas.getAck()) {
                if (removedFlag || resultValue == null) {
                    return NOT_FOUND();
                } else {
                    return OK(resultValue);
                }
            }
        }
        return GATEWAY_TIMEOUT();
    }

    public Response putObject(String key, byte[] value, String replicasString, boolean isReplication) {
        Replicas replicas = getReplicasFromString(replicasString);

        if (replicas == null)
            return BAD_REQUEST();

        if (isReplication)
            return storageService.putObject(key, value);

        int ackCount = 0;
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));

        for (int i = 0; i < replicas.getFrom(); i++) {
            Response response;

            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.putObject(key, value);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), METHOD_PUT, key, value);
            }

            if (response != null && response.code() == STATUS_CREATED) {
                ackCount++;
            }
        }
        if (ackCount >= replicas.getAck())
            return CREATED();

        return GATEWAY_TIMEOUT();
    }

    public Response removeObject(String key, String replicasString, boolean isReplication) {
        Replicas replicas = getReplicasFromString(replicasString);

        if (replicas == null)
            return BAD_REQUEST();

        if (isReplication)
            return storageService.removeObject(key);

        int ackCount = 0;
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));

        for (int i = 0; i < replicas.getFrom(); i++) {
            Response response;
            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.removeObject(key);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), METHOD_DELETE, key, null);
            }

            if (response != null && response.code() == STATUS_ACCEPTED) {
                ackCount++;
            }
        }
        if (ackCount >= replicas.getAck())
            return ACCEPTED();

        return GATEWAY_TIMEOUT();
    }

    private Response sendReplicationRequest(Node clusterNode, int method, String id, byte[] body) {
        Request.Builder builder = new Request.Builder()
                .url(clusterNode.getConnectionString() + REPLICATION_REQUEST_URL + id)
                .header(REPLICATION_HEADER, "true");

        switch (method) {
            case METHOD_GET:
                builder.get();
                break;
            case METHOD_PUT:
                builder.put(RequestBody.create(null, body));
                break;
            case METHOD_DELETE:
                builder.delete();
                break;
        }

        try {
            return clusterNode.getHttpClient().newCall(builder.build()).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Replicas getReplicasFromString(String replicas) {
        if (replicas == null)
            return Replicas.getDefault(topology.size());

        String[] split = replicas.split("/");
        int ack = Integer.valueOf(split[0]);
        int from = Integer.valueOf(split[1]);

        if (ack > 0 && ack <= from)
            return new Replicas(ack, from);
        return null;
    }

    private List<Node> getNodesSortedByDistance(String hash) {
        return clusterNodes.stream()
                .sorted(Comparator.comparingInt(node -> node.getDistance(hash)))
                .collect(Collectors.toList());
    }
}
