package ru.mail.polis.maxciv;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

@Indices({
        @Index(value = "keyHex", type = IndexType.NonUnique),
})
public class KeyValueObject {

    private String keyHex;
    private byte[] key;
    private byte[] value;

    public KeyValueObject() {
    }

    public KeyValueObject(String keyHex, byte[] key, byte[] value) {
        this.keyHex = keyHex;
        this.key = key;
        this.value = value;
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

    public void setValue(byte[] value) {
        this.value = value;
    }
}
