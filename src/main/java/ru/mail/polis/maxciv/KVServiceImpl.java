package ru.mail.polis.maxciv;

import one.nio.http.HttpServer;
import one.nio.http.HttpSession;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.KVService;
import ru.mail.polis.maxciv.cluster.ClusterController;
import ru.mail.polis.maxciv.cluster.NodesManager;

import java.io.IOException;
import java.util.Set;

import static one.nio.http.Response.ok;
import static ru.mail.polis.maxciv.util.CommonUtils.createServerConfig;
import static ru.mail.polis.maxciv.util.ResponceUtils.badRequest;

public class KVServiceImpl extends HttpServer implements KVService {

    private final ClusterController nodesManager;

    public KVServiceImpl(int port, KVDao dao, Set<String> topology) throws IOException {
        super(createServerConfig(port));
        this.nodesManager = new NodesManager(port, dao, topology);
    }

    @Path("/v0/status")
    public Response handleStatus() {
        return ok("Status: OK");
    }

    @Path("/v0/entity")
    public Response handleEntity(
            Request request,
            @Param(value = "id") String id,
            @Param(value = "replicas") String replicasString
    ) {
        return nodesManager.handleEntityRequest(request, id, request.getBody(), replicasString);
    }

    @Path("/v0/replica/entity")
    public Response handleReplicaEntity(
            Request request,
            @Param(value = "id") String id
    ) {
        return nodesManager.handleLocalEntityRequest(request, id, request.getBody());
    }

    @Override
    public void handleDefault(Request request, HttpSession session) throws IOException {
        session.sendResponse(badRequest());
    }

    @Override
    public synchronized void stop() {
        super.stop();
        nodesManager.getExecutorService().shutdown();
    }
}
