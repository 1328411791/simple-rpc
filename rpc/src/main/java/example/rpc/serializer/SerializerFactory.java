package example.rpc.serializer;

import example.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JsonSerializer();

    public static Serializer getSerializer(String key){
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
