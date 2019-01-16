package ru.mail.polis.maxciv.cluster;

import one.nio.http.Request;
import one.nio.http.Response;

import java.util.concurrent.ExecutorService;

public interface ClusterController {
    Response handleEntityRequest(Request request, String id, byte[] body, String replicasString);
    Response handleLocalEntityRequest(Request request, String id, byte[] body);
    ExecutorService getExecutorService();
}
