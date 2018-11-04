package ru.mail.polis.maxciv.util;

import one.nio.http.HttpServerConfig;
import one.nio.server.AcceptorConfig;
import org.apache.commons.codec.digest.DigestUtils;

public final class KVUtils {

    private KVUtils() {
    }

    public static String bytesToMd5Hex(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    public static HttpServerConfig createServerConfig(int port) {
        AcceptorConfig ac = new AcceptorConfig();
        ac.port = port;

        HttpServerConfig config = new HttpServerConfig();
        config.acceptors = new AcceptorConfig[]{ac};
        return config;
    }

    public static int getMd5StringsDistance(String a, String b) {
        int distance = 0;
        for (int i = 0; i < 32; i++) {
            distance += a.charAt(i) == b.charAt(i) ? 0 : 1;
        }
        return distance;
    }
}
