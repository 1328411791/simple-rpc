package example.rpc.serializer;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class JsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return null;
    }
}
