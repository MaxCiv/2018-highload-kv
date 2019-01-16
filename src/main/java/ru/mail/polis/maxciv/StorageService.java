package ru.mail.polis.maxciv;

import okhttp3.Response;
import ru.mail.polis.KVDao;
import ru.mail.polis.maxciv.data.KVObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static ru.mail.polis.maxciv.util.ResponceUtils.ACCEPTED;
import static ru.mail.polis.maxciv.util.ResponceUtils.CREATED;
import static ru.mail.polis.maxciv.util.ResponceUtils.NOT_FOUND;
import static ru.mail.polis.maxciv.util.ResponceUtils.OK;

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

            Map<String, String> map = new HashMap<>();
            map.put(ENTITY_TIMESTAMP_HEADER, String.valueOf(object.getTimestamp().getTime()));
            if (object.getRemoved())
                map.put(ENTITY_REMOVED_HEADER, "true");

            response = OK(object.getValue(), map);
        } catch (NoSuchElementException e) {
            response = NOT_FOUND();
        }
        return response;
    }

    public Response putObject(String key, byte[] value) {
        dao.upsert(key.getBytes(Charset.forName("UTF-8")), value);
        return CREATED();
    }

    public Response removeObject(String key) {
        dao.setRemoved(key.getBytes(Charset.forName("UTF-8")));
        return ACCEPTED();
    }
}
