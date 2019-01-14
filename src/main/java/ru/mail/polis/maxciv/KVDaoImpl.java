package ru.mail.polis.maxciv;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVDao;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public class KVDaoImpl implements KVDao {

    private static final KVObject KV_OBJECT = new KVObject();
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
//        KVObject keyValueObject = repository.find(eq("keyHex", bytesToMd5Hex(key))).firstOrDefault();
//        if (keyValueObject == null) throw new NoSuchElementException();
//        return keyValueObject.getValue();
        return "fdfdf".getBytes();
    }

    @Override
    public void upsert(@NotNull byte[] key, @NotNull byte[] value) throws IOException {
//        String keyHex = bytesToMd5Hex(key);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        repository.update(eq("keyHex", keyHex), new KVObject(keyHex, key, value, timestamp), true);
//        wrk -t12 -c400 -d30s "http://127.0.0.1:8080/v0/entity?replicas=3/3&id=PBQ1Q0I6XV1ZTLU57L41BD8IHT3TSXDKPW774UZA5FZK7U9HTPG4B70MW7MDR9KUEKQ9Z93OICGX2YIUMBCD9OV6F4VHRW50YZA5PAADQ747XZ80WYZVPH84KALCQNF"
    }

    @Override
    public void remove(@NotNull byte[] key) throws IOException {
//        repository.remove(eq("keyHex", bytesToMd5Hex(key)));
    }

    @Override
    public void close() throws IOException {
        repository.close();
        db.close();
    }

    @NotNull
    KVObject getObject(@NotNull byte[] key) throws NoSuchElementException {
//        KVObject keyValueObject = repository.find(eq("keyHex", bytesToMd5Hex(key))).firstOrDefault();
//        if (keyValueObject == null) throw new NoSuchElementException();
//        return keyValueObject;
        return KV_OBJECT;
    }

    void setRemoved(@NotNull byte[] key) {
        try {
//            KVObject keyValueObject = getObject(key);
//            keyValueObject.setRemoved(true);
//            repository.update(eq("keyHex", keyValueObject.getKeyHex()), keyValueObject, false);
        } catch (NoSuchElementException e) {
            return;
        }
    }
}
