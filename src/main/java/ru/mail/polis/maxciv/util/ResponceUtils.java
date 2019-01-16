package ru.mail.polis.maxciv.util;


import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Map;

public final class ResponceUtils {

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_GATEWAY_TIMEOUT = 504;

//    public static final Response CREATED = new Response(Response.CREATED, Response.EMPTY);
//    public static final Response ACCEPTED = new Response(Response.ACCEPTED, Response.EMPTY);
//    public static final Response BAD_REQUEST = new Response(Response.BAD_REQUEST, Response.EMPTY);
//    public static final Response NOT_FOUND = new Response(Response.NOT_FOUND, Response.EMPTY);
//    public static final Response METHOD_NOT_ALLOWED = new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
//    public static final Response GATEWAY_TIMEOUT = new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);

    private ResponceUtils() {
    }

//    public static Response CREATED() {
//        return new Response(Response.CREATED, Response.EMPTY);
//    }
//    public static Response ACCEPTED() {
//        return new Response(Response.ACCEPTED, Response.EMPTY);
//    }
//    public static Response BAD_REQUEST() {
//        return new Response(Response.BAD_REQUEST, Response.EMPTY);
//    }
//    public static Response NOT_FOUND() {
//        return new Response(Response.NOT_FOUND, Response.EMPTY);
//    }
//    public static Response METHOD_NOT_ALLOWED() {
//        return new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
//    }
//    public static Response GATEWAY_TIMEOUT() {
//        return new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
//    }

    public static Response OK() {
        return new Response.Builder()
                .code(STATUS_OK)
                .build();
    }

    public static Response OK(byte[] body) {
        return new Response.Builder()
                .code(STATUS_OK)
                .body(ResponseBody.create(null, body))
                .build();
    }

    public static Response OK(byte[] body, Map<String, String> headers) {
        Response.Builder builder = new Response.Builder();
        headers.keySet().forEach(name -> builder.addHeader(name, headers.get(name)));
        return builder
                .code(STATUS_OK)
                .body(ResponseBody.create(null, body))
                .build();
    }

    public static Response CREATED() {
        return new Response.Builder()
                .code(STATUS_CREATED)
                .build();
    }

    public static Response ACCEPTED() {
        return new Response.Builder()
                .code(STATUS_ACCEPTED)
                .build();
    }

    public static Response BAD_REQUEST() {
        return new Response.Builder()
                .code(STATUS_BAD_REQUEST)
                .build();
    }

    public static Response NOT_FOUND() {
        return new Response.Builder()
                .code(STATUS_NOT_FOUND)
                .build();
    }

    public static Response METHOD_NOT_ALLOWED() {
        return new Response.Builder()
                .code(STATUS_METHOD_NOT_ALLOWED)
                .build();
    }

    public static Response GATEWAY_TIMEOUT() {
        return new Response.Builder()
                .code(STATUS_GATEWAY_TIMEOUT)
                .build();
    }
}
