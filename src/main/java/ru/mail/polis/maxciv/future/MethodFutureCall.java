package ru.mail.polis.maxciv.future;

import ru.mail.polis.maxciv.StorageService;
import ru.mail.polis.maxciv.cluster.Node;

import java.util.concurrent.Callable;

abstract public class MethodFutureCall<T> implements Callable<T> {

    static final String REPLICATION_REQUEST_URL = "/v0/replica/entity?id=";

    private final Node node;
    private final StorageService storageService;
    private final String key;
    private final boolean isLocal;

    public MethodFutureCall(Node node, StorageService storageService, String key, boolean isLocal) {
        this.node = node;
        this.storageService = storageService;
        this.key = key;
        this.isLocal = isLocal;
    }

    public Node getNode() {
        return node;
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public String getKey() {
        return key;
    }

    public boolean isLocal() {
        return isLocal;
    }
}
