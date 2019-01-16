package ru.mail.polis.maxciv.util;

import one.nio.http.Response;

public final class ResponceUtils {

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_NOT_FOUND = 404;

    private ResponceUtils() {
    }

    public static Response created() {
        return new Response(Response.CREATED, Response.EMPTY);
    }
    public static Response accepted() {
        return new Response(Response.ACCEPTED, Response.EMPTY);
    }
    public static Response badRequest() {
        return new Response(Response.BAD_REQUEST, Response.EMPTY);
    }
    public static Response notFound() {
        return new Response(Response.NOT_FOUND, Response.EMPTY);
    }
    public static Response methodNotAllowed() {
        return new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
    }
    public static Response internalError() {
        return new Response(Response.INTERNAL_ERROR, Response.EMPTY);
    }
    public static Response gatewayTimeout() {
        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
    }
}
