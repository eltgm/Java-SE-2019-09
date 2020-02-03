package ru.otus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    public Serializer() {

    }

    public byte[] serialize(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(data);
            os.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Serialization error:" + e.getMessage());
        }
    }

    public <T> T deserialize(byte[] data, Class<T> classOfT) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream is = new ObjectInputStream(bis)) {
            Object obj = is.readObject();
            return classOfT.cast(obj);
        } catch (Exception e) {
            throw new RuntimeException("DeSerialization error:" + e.getMessage());
        }
    }
}
