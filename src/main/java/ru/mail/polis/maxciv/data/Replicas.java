package ru.mail.polis.maxciv.data;

public class Replicas {

    private final int ack;
    private final int from;

    public Replicas(int ack, int from) {
        this.ack = ack;
        this.from = from;
    }

    public static Replicas getDefault(final int from) {
        int ack = from / 2 + 1;
        return new Replicas(ack, from);
    }

    public int getAck() {
        return ack;
    }

    public int getFrom() {
        return from;
    }
}
