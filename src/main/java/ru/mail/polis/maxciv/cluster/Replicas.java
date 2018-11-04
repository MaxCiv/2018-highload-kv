package ru.mail.polis.maxciv.cluster;

public class Replicas {

    private final int ack;
    private final int from;

    public Replicas(int ack, int from) {
        this.ack = ack;
        this.from = from;
    }

    public static Replicas getDefault(final int from) {
        int ack = from % 2 == 0
                ? from / 2 + 1
                : (int) Math.round(((double) from) / 2.0);
        return new Replicas(ack, from);
    }

    int getAck() {
        return ack;
    }

    int getFrom() {
        return from;
    }
}
