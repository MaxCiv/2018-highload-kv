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
    private Boolean isRemoved;

    public KVObject() {
    }

    public KVObject(String keyHex, byte[] key, byte[] value, Timestamp timestamp) {
        this.keyHex = keyHex;
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getKeyHex() {
        return keyHex;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Boolean getRemoved() {
        return isRemoved;
    }

    public void setRemoved(Boolean removed) {
        isRemoved = removed;
    }
}
