package ru.mail.polis.maxciv.util;

import one.nio.http.HttpServerConfig;
import one.nio.server.AcceptorConfig;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static String bytesToSha3Hex(byte[] bytes) {
        return Hex.toHexString(new SHA3.Digest256().digest(bytes));
    }

    public static HttpServerConfig createServerConfig(int port) {
        AcceptorConfig ac = new AcceptorConfig();
        ac.port = port;

        HttpServerConfig config = new HttpServerConfig();
        config.acceptors = new AcceptorConfig[]{ac};
        return config;
    }

    public static int getSha3StringsDistance(String a, String b) {
        int distance = 0;
        for (int i = 0; i < 32; i++) {
            distance += a.charAt(i) == b.charAt(i) ? 0 : 1;
        }
        return distance;
    }
}
