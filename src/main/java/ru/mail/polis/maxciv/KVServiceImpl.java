package ru.mail.polis.maxciv;

import okhttp3.Response;
import one.nio.http.HttpServer;
import one.nio.http.HttpSession;
import one.nio.http.Param;
import one.nio.http.Path;
import one.nio.http.Request;
import ru.mail.polis.KVDao;
import ru.mail.polis.KVService;
import ru.mail.polis.maxciv.cluster.ClusterService;

import java.io.IOException;
import java.util.Set;

import static ru.mail.polis.maxciv.util.CommonUtils.createServerConfig;
import static ru.mail.polis.maxciv.util.ResponceUtils.BAD_REQUEST;
import static ru.mail.polis.maxciv.util.ResponceUtils.METHOD_NOT_ALLOWED;
import static ru.mail.polis.maxciv.util.ResponceUtils.OK;

public class KVServiceImpl extends HttpServer implements KVService {

    private final ClusterService clusterService;

    public KVServiceImpl(int port, KVDao dao, Set<String> topology) throws IOException {
        super(createServerConfig(port));
        this.clusterService = new ClusterService(port, dao, topology);
    }

    @Path("/v0/status")
    public Response handleStatus() {
        return OK();
    }

    @Path("/v0/entity")
    public Response handleEntity(
            Request request,
            @Param(value = "id") String id,
            @Param(value = "replicas") String replicasString
    ) {
        if (id == null || id.isEmpty() || (replicasString != null && replicasString.isEmpty()))
            return BAD_REQUEST();

        boolean isReplication = request.getHeader(ClusterService.REPLICATION_HEADER) != null;

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return clusterService.getObject(id, replicasString, isReplication);
            case Request.METHOD_PUT:
                return clusterService.putObject(id, request.getBody(), replicasString, isReplication);
            case Request.METHOD_DELETE:
                return clusterService.removeObject(id, replicasString, isReplication);
            default:
                return METHOD_NOT_ALLOWED();
        }
    }

    @Override
    public void handleDefault(Request request, HttpSession session) throws IOException {
        session.sendResponse(new one.nio.http.Response(one.nio.http.Response.BAD_REQUEST));
    }
}
