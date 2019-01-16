package ru.mail.polis.maxciv.future;

import one.nio.http.Response;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.cluster.Node;

import static ru.mail.polis.maxciv.util.ResponceUtils.internalError;

public class DeleteCall extends MethodFutureCall<Response> {

    public DeleteCall(Node node, StorageService storageService, String key, boolean isLocal) {
        super(node, storageService, key, isLocal);
    }

    @Override
    public Response call() {
        if (isLocal()) return localDelete();
        return delete();
    }

    private Response delete() {
        try {
            return getNode().getHttpClient().delete(REPLICATION_REQUEST_URL + getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return internalError();
        }
    }

    private Response localDelete() {
        return getStorageService().deleteObject(getKey());
    }
}
