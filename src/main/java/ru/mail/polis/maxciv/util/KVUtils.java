package ru.mail.polis.maxciv.util;

import one.nio.http.HttpServerConfig;
import one.nio.server.AcceptorConfig;
import org.apache.commons.codec.digest.DigestUtils;

public final class KVUtils {

    private KVUtils() {
    }

    public static String bytesToHex(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    public static HttpServerConfig createServerConfig(int port) {
        AcceptorConfig ac = new AcceptorConfig();
        ac.port = port;

        HttpServerConfig config = new HttpServerConfig();
        config.acceptors = new AcceptorConfig[]{ac};
        return config;
    }
}
