package ru.mail.polis.maxciv;

import one.nio.http.*;
import ru.mail.polis.KVDao;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

import static ru.mail.polis.maxciv.util.KVUtils.createServerConfig;

public class KVServiceImpl extends HttpServer implements KVService {

    private final KVDao dao;

    public KVServiceImpl(int port, KVDao dao) throws IOException {
        super(createServerConfig(port));
        this.dao = dao;
    }

    @Path("/v0/status")
    public Response handleStatus() {
        return Response.ok("Status: OK");
    }

    @Path("/v0/entity")
    public Response handleEntity(Request request, @Param(value = "id") String id) {
        if (id == null || id.isEmpty()) {
            return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return getEntity(id);
            case Request.METHOD_PUT:
                return putEntity(id, request);
            case Request.METHOD_DELETE:
                return deleteEntity(id);
            default:
                return new Response(Response.BAD_REQUEST, Response.EMPTY);
        }
    }

    @Override
    public void handleDefault(Request request, HttpSession session) throws IOException {
        Response response = new Response(Response.NOT_FOUND, Response.EMPTY);
        session.sendResponse(response);
    }

    private Response getEntity(String id) {
        try {
            byte[] bytes = dao.get(id.getBytes(Charset.forName("UTF-8")));
            return Response.ok(bytes);
        } catch (NoSuchElementException e) {
            return new Response(Response.NOT_FOUND, Response.EMPTY);
        } catch (Exception e) {
            return new Response(Response.INTERNAL_ERROR, Response.EMPTY);
        }
    }

    private Response putEntity(String id, Request request) {
        try {
            dao.upsert(id.getBytes(Charset.forName("UTF-8")), request.getBody());
            return new Response(Response.CREATED, Response.EMPTY);
        } catch (Exception e) {
            return new Response(Response.INTERNAL_ERROR, Response.EMPTY);
        }
    }

    private Response deleteEntity(String id) {
        try {
            dao.remove(id.getBytes(Charset.forName("UTF-8")));
            return new Response(Response.ACCEPTED, Response.EMPTY);
        } catch (Exception e) {
            return new Response(Response.INTERNAL_ERROR, Response.EMPTY);
        }
    }
}
