package ru.mail.polis.maxciv.future;

import one.nio.http.Response;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.cluster.Node;

import static ru.mail.polis.maxciv.util.ResponceUtils.internalError;

public class PutCall extends MethodFutureCall<Response> {

    private final byte[] value;

    public PutCall(Node node, StorageService storageService, String key, boolean isLocal, byte[] value) {
        super(node, storageService, key, isLocal);
        this.value = value;
    }

    @Override
    public Response call() {
        if (isLocal()) return localPut();
        return put();
    }

    private Response put() {
        try {
            return getNode().getHttpClient().put(REPLICATION_REQUEST_URL + getKey(), value);
        } catch (Exception e) {
            e.printStackTrace();
            return internalError();
        }
    }

    private Response localPut() {
        return getStorageService().putObject(getKey(), value);
    }
}
