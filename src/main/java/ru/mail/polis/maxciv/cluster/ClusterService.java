package ru.mail.polis.maxciv.cluster;

import one.nio.http.Request;
import one.nio.http.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.util.KVUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClusterService {

    public static final String REPLICATION_HEADER = "Replication: ";

    private static final String REPLICATION_REQUEST_URL = "/v0/entity?id=";
    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_ACCEPTED = 202;
    private static final int STATUS_NOT_FOUND = 404;

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
            return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }

        if (isReplication) {
            return storageService.getObject(key);
        }

        int ackCount = 0;
        boolean removedFlag = false;
        byte[] resultObject = null;
        long mostFreshTimestamp = 0;
        String keyHash = KVUtils.bytesToMd5Hex(key.getBytes());
        List<Node> sortedNodes = getNodesSortedByDistances(keyHash);

        for (int i = 0; i < replicas.getFrom(); i++) {
            Response response;

            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.getObject(key);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), Request.METHOD_GET, key, null);
            }

            if (response != null) {
                if (response.getStatus() == STATUS_OK || response.getStatus() == STATUS_NOT_FOUND) {
                    ackCount++;
                    if (response.getStatus() == STATUS_OK) {
                        String timestamp = response.getHeader(StorageService.ENTITY_TIMESTAMP_HEADER);
                        Long objectTimestamp = new Long(timestamp);

                        if (response.getHeader(StorageService.ENTITY_REMOVED_HEADER) != null) {
                            removedFlag = true;
                        } else if (objectTimestamp > mostFreshTimestamp) {
                            mostFreshTimestamp = objectTimestamp;
                            resultObject = response.getBody();
                        }
                    }
                }
            }
            if (ackCount >= replicas.getAck()) {
                if (removedFlag || resultObject == null) {
                    return new Response(Response.NOT_FOUND, Response.EMPTY);
                } else {
                    return new Response(Response.OK, resultObject);
                }
            }
        }
        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
    }

    public Response putObject(String key, byte[] value, String replicasString, boolean isReplication) {
        Replicas replicasObj = getReplicasFromString(replicasString);

        if (replicasObj == null)
            return new Response(Response.BAD_REQUEST, Response.EMPTY);

        if (isReplication)
            return storageService.putObject(key, value);

        int ackCount = 0;
        String keyHash = KVUtils.bytesToMd5Hex(key.getBytes());
        List<Node> sortedNodes = getNodesSortedByDistances(keyHash);
        for (int i = 0; i < replicasObj.getFrom(); i++) {
            Response response;

            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.putObject(key, value);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), Request.METHOD_PUT, key, value);
            }

            if (response != null && response.getStatus() == STATUS_CREATED) {
                ackCount++;
            }
        }
        if (ackCount >= replicasObj.getAck()) {
            return new Response(Response.CREATED, Response.EMPTY);
        }

        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
    }

    public Response removeObject(String key, String replicasString, boolean isReplication) {
        Replicas replicasObj = getReplicasFromString(replicasString);

        if (replicasObj == null)
            return new Response(Response.BAD_REQUEST, Response.EMPTY);

        if (isReplication)
            return storageService.removeObject(key);

        int ackCount = 0;
        String keyHash = KVUtils.bytesToMd5Hex(key.getBytes());
        List<Node> sortedNodes = getNodesSortedByDistances(keyHash);
        for (int i = 0; i < replicasObj.getFrom(); i++) {
            Response response;
            if (sortedNodes.get(i).getPort() == currentPort) {
                response = storageService.removeObject(key);
            } else {
                response = sendReplicationRequest(sortedNodes.get(i), Request.METHOD_DELETE, key, null);
            }

            if (response != null && response.getStatus() == STATUS_ACCEPTED) {
                ackCount++;
            }
        }
        if (ackCount >= replicasObj.getAck()) {
            return new Response(Response.ACCEPTED, Response.EMPTY);
        }
        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
    }


    private Response sendReplicationRequest(Node clusterNode, int method, String id, byte[] body) {
        Request request = new Request(method, REPLICATION_REQUEST_URL + id, true);
        request.addHeader(REPLICATION_HEADER + true);

        if (body != null) {
            request.addHeader("Content-Length: " + body.length);
            request.setBody(body);
        }
        try {
            return clusterNode.getHttpClient().invoke(request);
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

    private List<Node> getNodesSortedByDistances(String hash) {
        return clusterNodes.stream()
                .sorted(Comparator.comparingInt(node -> node.getDistance(hash)))
                .collect(Collectors.toList());
    }
}
