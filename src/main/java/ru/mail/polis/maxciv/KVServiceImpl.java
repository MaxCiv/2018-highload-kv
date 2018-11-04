package ru.mail.polis.maxciv;

import one.nio.http.HttpServer;
import one.nio.http.HttpSession;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import one.nio.http.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.KVService;
import ru.mail.polis.maxciv.cluster.ClusterService;

import java.io.IOException;
import java.util.Set;

import static ru.mail.polis.maxciv.util.KVUtils.createServerConfig;

public class KVServiceImpl extends HttpServer implements KVService {

    private final ClusterService clusterService;

    public KVServiceImpl(int port, KVDao dao, Set<String> topology) throws IOException {
        super(createServerConfig(port));
        this.clusterService = new ClusterService(port, dao, topology);
    }

    @Path("/v0/status")
    public Response handleStatus() {
        return Response.ok("Status: OK");
    }

    @Path("/v0/entity")
    public Response handleEntity(
            Request request,
            @Param(value = "id") String id,
            @Param(value = "replicas") String replicas
    ) {
        if (id == null || id.isEmpty() || (replicas != null && replicas.isEmpty()))
            return new Response(Response.BAD_REQUEST, Response.EMPTY);

        boolean isReplication = request.getHeader(ClusterService.REPLICATION_HEADER) != null;

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return clusterService.getObject(id, replicas, isReplication);
            case Request.METHOD_PUT:
                return clusterService.putObject(id, request.getBody(), request, replicas, isReplication);
            case Request.METHOD_DELETE:
                return clusterService.removeObject(id, replicas, isReplication);
            default:
                return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }
    }

    @Override
    public void handleDefault(Request request, HttpSession session) throws IOException {
        Response response = new Response(Response.BAD_REQUEST, Response.EMPTY);
        session.sendResponse(response);
    }
}
