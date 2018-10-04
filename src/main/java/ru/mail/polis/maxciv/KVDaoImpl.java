package ru.mail.polis.maxciv;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVDao;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static ru.mail.polis.maxciv.util.KVUtils.bytesToHex;

public class KVDaoImpl implements KVDao {

    private ObjectRepository<KeyValueObject> repository;
    private Nitrite db;

    public KVDaoImpl(@NotNull File baseDir) {
        db = Nitrite.builder()
                .filePath(baseDir.getPath() + File.separator + "key_value.db")
                .openOrCreate();

        repository = db.getRepository(KeyValueObject.class);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull byte[] key) throws NoSuchElementException, IOException {
        KeyValueObject keyValueObject = repository.find(eq("keyHex", bytesToHex(key))).firstOrDefault();
        if (keyValueObject == null) throw new NoSuchElementException();
        return keyValueObject.getValue();
    }

    @Override
    public void upsert(@NotNull byte[] key, @NotNull byte[] value) throws IOException {
        String keyHex = bytesToHex(key);
        repository.update(eq("keyHex", keyHex), new KeyValueObject(keyHex, key, value), true);
    }

    @Override
    public void remove(@NotNull byte[] key) throws IOException {
        repository.remove(eq("keyHex", bytesToHex(key)));
    }

    @Override
    public void close() throws IOException {
        repository.close();
        db.close();
    }
}
