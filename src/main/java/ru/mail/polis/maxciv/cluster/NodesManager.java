package ru.mail.polis.maxciv.cluster;

import one.nio.http.Request;
import one.nio.http.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.data.ReplicasConfig;
import ru.mail.polis.maxciv.future.Caller;
import ru.mail.polis.maxciv.future.DeleteCall;
import ru.mail.polis.maxciv.future.GetCall;
import ru.mail.polis.maxciv.future.PutCall;
import ru.mail.polis.maxciv.util.CommonUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static one.nio.http.Response.ok;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_ACCEPTED;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_CREATED;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_NOT_FOUND;
import static ru.mail.polis.maxciv.util.ResponceUtils.STATUS_OK;
import static ru.mail.polis.maxciv.util.ResponceUtils.accepted;
import static ru.mail.polis.maxciv.util.ResponceUtils.badRequest;
import static ru.mail.polis.maxciv.util.ResponceUtils.created;
import static ru.mail.polis.maxciv.util.ResponceUtils.gatewayTimeout;
import static ru.mail.polis.maxciv.util.ResponceUtils.methodNotAllowed;
import static ru.mail.polis.maxciv.util.ResponceUtils.notFound;

public class NodesManager implements ClusterController {

    private final StorageService localStorageService;
    private final int currentPort;
    private final Set<String> topology;
    private final List<Node> clusterNodes;
    private final ExecutorService executorService;

    public NodesManager(int port, KVDao dao, Set<String> topology) {
        this.localStorageService = new StorageService(dao);
        this.currentPort = port;
        this.topology = topology;
        this.clusterNodes = topology.stream().map(Node::new).collect(Collectors.toList());
        this.executorService = Executors.newWorkStealingPool();
    }

    @Override
    public Response handleLocalEntityRequest(Request request, String id, byte[] body) {
        if (id == null || id.isEmpty())
            return badRequest();

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return localStorageService.getObject(id);
            case Request.METHOD_PUT:
                return localStorageService.putObject(id, request.getBody());
            case Request.METHOD_DELETE:
                return localStorageService.deleteObject(id);
            default:
                return methodNotAllowed();
        }
    }

    @Override
    public Response handleEntityRequest(Request request, String id, byte[] body, String replicasString) {
        ReplicasConfig replicasConfig = ReplicasConfig.getReplicasFromString(replicasString, topology.size());
        if (id == null || id.isEmpty() || replicasConfig == null)
            return badRequest();

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return getObject(id, replicasConfig);
            case Request.METHOD_PUT:
                return putObject(id, request.getBody(), replicasConfig);
            case Request.METHOD_DELETE:
                return deleteObject(id, replicasConfig);
            default:
                return methodNotAllowed();
        }
    }

    private Response getObject(String key, ReplicasConfig replicasConfig) {
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));
        List<Callable<Response>> calls = sortedNodes.stream()
                .map(node -> new GetCall(node, localStorageService, key, (node.getPort() == currentPort)))
                .collect(Collectors.toList());

        List<Response> getResponses = new Caller<Response>(executorService).makeAllCallsInParallel(calls);

        int ackCount = 0;
        boolean removedFlag = false;
        byte[] resultValue = null;
        long newerTimestamp = 0;
        for (Response response : getResponses) {
            if (response != null && (response.getStatus() == STATUS_OK || response.getStatus() == STATUS_NOT_FOUND)) {
                ackCount++;
                if (response.getStatus() == STATUS_OK) {
                    String timestamp = response.getHeader(StorageService.ENTITY_TIMESTAMP_HEADER);
                    Long objectTimestamp = Long.parseLong(timestamp);

                    if (response.getHeader(StorageService.ENTITY_REMOVED_HEADER) != null) {
                        removedFlag = true;
                    } else if (objectTimestamp > newerTimestamp) {
                        newerTimestamp = objectTimestamp;
                        resultValue = response.getBody();
                    }
                }
            }
            if (ackCount >= replicasConfig.getAck()) {
                if (removedFlag || resultValue == null) {
                    return notFound();
                } else {
                    return ok(resultValue);
                }
            }
        }
        return gatewayTimeout();
    }

    private Response putObject(String key, byte[] value, ReplicasConfig replicasConfig) {
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));
        List<Callable<Response>> calls = sortedNodes.stream()
                .map(node -> new PutCall(node, localStorageService, key, (node.getPort() == currentPort), value))
                .collect(Collectors.toList());

        List<Response> putResponses = new Caller<Response>(executorService).makeAllCallsInParallel(calls);

        int ackCount = 0;
        for (Response response : putResponses) {
            if (response != null && response.getStatus() == STATUS_CREATED) {
                ackCount++;
            }
        }
        if (ackCount >= replicasConfig.getAck())
            return created();

        return gatewayTimeout();
    }

    private Response deleteObject(String key, ReplicasConfig replicasConfig) {
        List<Node> sortedNodes = getNodesSortedByDistance(CommonUtils.bytesToSha3Hex(key.getBytes()));
        List<Callable<Response>> calls = sortedNodes.stream()
                .map(node -> new DeleteCall(node, localStorageService, key, (node.getPort() == currentPort)))
                .collect(Collectors.toList());

        List<Response> deleteResponses = new Caller<Response>(executorService).makeAllCallsInParallel(calls);

        int ackCount = 0;
        for (Response response : deleteResponses) {
            if (response != null && response.getStatus() == STATUS_ACCEPTED) {
                ackCount++;
            }
        }
        if (ackCount >= replicasConfig.getAck())
            return accepted();

        return gatewayTimeout();
    }

    private List<Node> getNodesSortedByDistance(String hash) {
        return clusterNodes.stream()
                .sorted(Comparator.comparingInt(node -> node.getDistance(hash)))
                .collect(Collectors.toList());
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
