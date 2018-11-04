package ru.mail.polis.maxciv;

import one.nio.http.Response;
import ru.mail.polis.KVDao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

public class StorageService {

    public static final String ENTITY_TIMESTAMP_HEADER = "Timestamp: ";
    public static final String ENTITY_REMOVED_HEADER = "Removed: ";

    private final KVDaoImpl dao;

    public StorageService(KVDao dao) {
        this.dao = (KVDaoImpl) dao;
    }

    public Response getObject(String key) {
        Response response;
        try {
            KVObject object = dao.getObject(key.getBytes(Charset.forName("UTF-8")));

            response = Response.ok(object.getValue());
            response.addHeader(ENTITY_TIMESTAMP_HEADER + object.getTimestamp().getTime());

            if (object.getRemoved())
                response.addHeader(ENTITY_REMOVED_HEADER + true);
        } catch (NoSuchElementException e) {
            response = new Response(Response.NOT_FOUND, Response.EMPTY);
        }
        return response;
    }

    public Response putObject(String key, byte[] value) {
        Response response;
        try {
            dao.upsert(key.getBytes(Charset.forName("UTF-8")), value);
            response = new Response(Response.CREATED, Response.EMPTY);
        } catch (IOException e) {
            response = new Response(Response.INTERNAL_ERROR, Response.EMPTY);
        }
        return response;
    }

    public Response removeObject(String key) {
        dao.setRemoved(key.getBytes(Charset.forName("UTF-8")));
        return new Response(Response.ACCEPTED, Response.EMPTY);
    }
}
