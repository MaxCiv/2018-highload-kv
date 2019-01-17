package ru.mail.polis.maxciv.cluster;

import one.nio.http.HttpClient;
import one.nio.net.ConnectionString;
import ru.mail.polis.maxciv.util.CommonUtils;

public class Node {

    private final String idSha3;
    private final int port;
    private final HttpClient httpClient;
    private final String connectionString;

    Node(String connectionString) {
        this.idSha3 = CommonUtils.bytesToSha3Hex(connectionString.getBytes());
        this.port = Integer.valueOf(connectionString.split(":")[2]);
        this.httpClient = new HttpClient(new ConnectionString(connectionString));
        this.connectionString = connectionString;
    }

    int getDistance(String stringSha3) {
        return CommonUtils.getSha3StringsDistance(idSha3, stringSha3);
    }

    public int getPort() {
        return port;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getConnectionString() {
        return connectionString;
    }
}
