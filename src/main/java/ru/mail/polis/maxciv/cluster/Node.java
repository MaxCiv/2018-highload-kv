package ru.mail.polis.maxciv.cluster;

import okhttp3.OkHttpClient;
import ru.mail.polis.maxciv.util.CommonUtils;

public class Node {

    private final String idSha3;
    private final String connectionString;
    private final int port;
    private final OkHttpClient httpClient;

    Node(String connectionString) {
        this.idSha3 = CommonUtils.bytesToSha3Hex(connectionString.getBytes());
        this.port = Integer.valueOf(connectionString.split(":")[2]);
        this.httpClient = new OkHttpClient();
        this.connectionString = connectionString;
    }

    int getDistance(String stringSha3) {
        return CommonUtils.getSha3StringsDistance(idSha3, stringSha3);
    }

    String getConnectionString() {
        return connectionString;
    }

    public int getPort() {
        return port;
    }

    OkHttpClient getHttpClient() {
        return httpClient;
    }
}
