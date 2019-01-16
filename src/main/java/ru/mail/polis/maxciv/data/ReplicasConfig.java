package ru.mail.polis.maxciv.data;

import org.jetbrains.annotations.Nullable;

public class ReplicasConfig {

    private final int ack;
    private final int from;

    public ReplicasConfig(int ack, int from) {
        this.ack = ack;
        this.from = from;
    }

    public static ReplicasConfig getDefault(final int from) {
        int ack = from / 2 + 1;
        return new ReplicasConfig(ack, from);
    }

    @Nullable
    public static ReplicasConfig getReplicasFromString(String replicas, final int topologySize) {
        if (replicas == null)
            return ReplicasConfig.getDefault(topologySize);

        String[] split = replicas.split("/");
        int ack = Integer.valueOf(split[0]);
        int from = Integer.valueOf(split[1]);

        if (ack > 0 && ack <= from)
            return new ReplicasConfig(ack, from);
        return null;
    }

    public int getAck() {
        return ack;
    }

    public int getFrom() {
        return from;
    }
}
