package ru.mail.polis.maxciv.util;

import one.nio.http.Response;

public final class ResponceUtils {

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_NOT_FOUND = 404;

//    public static final Response CREATED = new Response(Response.CREATED, Response.EMPTY);
//    public static final Response ACCEPTED = new Response(Response.ACCEPTED, Response.EMPTY);
//    public static final Response BAD_REQUEST = new Response(Response.BAD_REQUEST, Response.EMPTY);
//    public static final Response NOT_FOUND = new Response(Response.NOT_FOUND, Response.EMPTY);
//    public static final Response METHOD_NOT_ALLOWED = new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
//    public static final Response GATEWAY_TIMEOUT = new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);

    private ResponceUtils() {
    }

    public static Response CREATED() {
        return new Response(Response.CREATED, Response.EMPTY);
    }
    public static Response ACCEPTED() {
        return new Response(Response.ACCEPTED, Response.EMPTY);
    }
    public static Response BAD_REQUEST() {
        return new Response(Response.BAD_REQUEST, Response.EMPTY);
    }
    public static Response NOT_FOUND() {
        return new Response(Response.NOT_FOUND, Response.EMPTY);
    }
    public static Response METHOD_NOT_ALLOWED() {
        return new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
    }
    public static Response GATEWAY_TIMEOUT() {
        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
    }
}
