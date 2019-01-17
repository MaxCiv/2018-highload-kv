package ru.mail.polis.maxciv;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.polis.KVDao;
import ru.mail.polis.maxciv.data.KVObject;

import java.io.File;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static ru.mail.polis.maxciv.util.CommonUtils.bytesToSha3Hex;

public class KVDaoImpl implements KVDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(KVDaoImpl.class);

    private final ObjectRepository<KVObject> repository;
    private final Cache<String, KVObject> cache;
    private final Nitrite db;

    public KVDaoImpl(@NotNull File baseDir) {
        db = Nitrite.builder()
                .filePath(baseDir.getPath() + File.separator + "key_value.db")
                .openOrCreate();
        repository = db.getRepository(KVObject.class);
        cache = new Cache2kBuilder<String, KVObject>() {}
                .eternal(true)
                .entryCapacity(100)
                .build();
    }

    @NotNull
    @Override
    public byte[] get(@NotNull byte[] key) throws NoSuchElementException {
        final String keyHex = bytesToSha3Hex(key);
        KVObject keyValueObject = cache.peek(keyHex);
        if (keyValueObject != null) return keyValueObject.getValue();

        keyValueObject = repository.find(eq("keyHex", keyHex)).firstOrDefault();
        if (keyValueObject == null) throw new NoSuchElementException();

        return keyValueObject.getValue();
    }

    @Override
    public void upsert(@NotNull byte[] key, @NotNull byte[] value) {
        final String keyHex = bytesToSha3Hex(key);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            KVObject kvObject = new KVObject(keyHex, key, value, timestamp);
            repository.update(eq("keyHex", keyHex), kvObject, true);
            cache.put(keyHex, kvObject);
        } catch (Exception e) {
            LOGGER.error("Error while upsert value with keyHex={}", keyHex, e);
        }
    }

    @Override
    public void remove(@NotNull byte[] key) {
        final String keyHex = bytesToSha3Hex(key);
        repository.remove(eq("keyHex", keyHex));
        cache.remove(keyHex);
    }

    @Override
    public void close() {
        repository.close();
        db.close();
    }

    @NotNull
    KVObject getObject(@NotNull byte[] key) throws NoSuchElementException {
        KVObject keyValueObject = repository.find(eq("keyHex", bytesToSha3Hex(key))).firstOrDefault();
        if (keyValueObject == null) throw new NoSuchElementException();
        return keyValueObject;
    }

    void setRemoved(@NotNull byte[] key) {
        try {
            KVObject keyValueObject = getObject(key);
            keyValueObject.setRemoved(true);
            repository.update(eq("keyHex", keyValueObject.getKeyHex()), keyValueObject, false);
        } catch (NoSuchElementException ignored) {
        }
    }
}
