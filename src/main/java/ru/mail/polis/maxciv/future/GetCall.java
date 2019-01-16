package ru.mail.polis.maxciv.future;

import one.nio.http.Response;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.cluster.Node;

import static ru.mail.polis.maxciv.util.ResponceUtils.internalError;

public class GetCall extends MethodFutureCall<Response> {

    public GetCall(Node node, StorageService storageService, String key, boolean isLocal) {
        super(node, storageService, key, isLocal);
    }

    @Override
    public Response call() {
        if (isLocal()) return localGet();
        return get();
    }

    private Response get() {
        try {
            return getNode().getHttpClient().get(REPLICATION_REQUEST_URL + getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return internalError();
        }
    }

    private Response localGet() {
        return getStorageService().getObject(getKey());
    }
}
