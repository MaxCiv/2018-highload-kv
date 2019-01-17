package ru.mail.polis.maxciv.future;

import one.nio.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.cluster.Node;

import static ru.mail.polis.maxciv.util.ResponceUtils.internalError;

public class PutCall extends MethodFutureCall<Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PutCall.class);

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
            LOGGER.error("Error while call remote PUT request to {}", getNode().getConnectionString(), e);
            return internalError();
        }
    }

    private Response localPut() {
        return getStorageService().putObject(getKey(), value);
    }
}
