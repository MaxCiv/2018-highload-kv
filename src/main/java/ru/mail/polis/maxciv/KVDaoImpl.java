package ru.mail.polis.maxciv;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVDao;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static ru.mail.polis.maxciv.util.KVUtils.bytesToMd5Hex;

public class KVDaoImpl implements KVDao {

    private final ObjectRepository<KVObject> repository;
    private final Nitrite db;

    public KVDaoImpl(@NotNull File baseDir) {
        db = Nitrite.builder()
                .filePath(baseDir.getPath() + File.separator + "key_value.db")
                .openOrCreate();

        repository = db.getRepository(KVObject.class);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull byte[] key) throws NoSuchElementException, IOException {
        KVObject keyValueObject = repository.find(eq("keyHex", bytesToMd5Hex(key))).firstOrDefault();
        if (keyValueObject == null) throw new NoSuchElementException();
        return keyValueObject.getValue();
    }

    @Override
    public void upsert(@NotNull byte[] key, @NotNull byte[] value) throws IOException {
        String keyHex = bytesToMd5Hex(key);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        repository.update(eq("keyHex", keyHex), new KVObject(keyHex, key, value, timestamp), true);
    }

    @Override
    public void remove(@NotNull byte[] key) throws IOException {
        repository.remove(eq("keyHex", bytesToMd5Hex(key)));
    }

    @Override
    public void close() throws IOException {
        repository.close();
        db.close();
    }

    @NotNull
    KVObject getObject(@NotNull byte[] key) throws NoSuchElementException {
        KVObject keyValueObject = repository.find(eq("keyHex", bytesToMd5Hex(key))).firstOrDefault();
        if (keyValueObject == null) throw new NoSuchElementException();
        return keyValueObject;
    }

    void setRemoved(@NotNull byte[] key) {
        try {
            KVObject keyValueObject = getObject(key);
            keyValueObject.setRemoved(true);
            repository.update(eq("keyHex", keyValueObject.getKeyHex()), keyValueObject, false);
        } catch (Exception e) {
            return;
        }
    }
}
