package ru.mail.polis.maxciv;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.sql.Timestamp;

@Indices({
        @Index(value = "keyHex", type = IndexType.NonUnique),
})
public class KVObject {

    private String keyHex;
    private byte[] key;
    private byte[] value;
    private Timestamp timestamp;
    private boolean isRemoved = false;

    public KVObject() {
    }

    KVObject(String keyHex, byte[] key, byte[] value, Timestamp timestamp) {
        this.keyHex = keyHex;
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }

    String getKeyHex() {
        return keyHex;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    Timestamp getTimestamp() {
        return timestamp;
    }

    boolean getRemoved() {
        return isRemoved;
    }

    void setRemoved(boolean removed) {
        isRemoved = removed;
    }
}
