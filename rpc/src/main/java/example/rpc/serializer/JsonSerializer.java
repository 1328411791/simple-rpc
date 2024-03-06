package example.rpc.serializer;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class JsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        Gson gson = new Gson();
        return gson.toJson(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(new String(bytes), clazz);
    }
}
