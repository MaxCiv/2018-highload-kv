package ru.mail.polis.maxciv.cluster;

import one.nio.http.HttpClient;
import one.nio.net.ConnectionString;
import ru.mail.polis.maxciv.util.KVUtils;

public class Node {

    private final String idMd5;
    private final int port;
    private final HttpClient httpClient;

    public Node(String connectionString) {
        this.idMd5 = KVUtils.bytesToMd5Hex(connectionString.getBytes());
        this.port = Integer.valueOf(connectionString.split(":")[2]);
        httpClient = new HttpClient(new ConnectionString(connectionString));
    }

    public int getDistance(String stringMd5) {
        return KVUtils.getMd5StringsDistance(idMd5, stringMd5);
    }

    public int getPort() {
        return port;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
