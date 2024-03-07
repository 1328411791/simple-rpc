package example.rpc.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    private static final Map<String, Serializer> serializerMap = new HashMap<>(){
        {
            put(SerializerKeys.HESSIAN, new HessianSerializer());
            put(SerializerKeys.JSON, new JsonSerializer());
            put(SerializerKeys.JDK, new JdkSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
        }
    };

    private static final Serializer DEFAULT_SERIALIZER = serializerMap.get(SerializerKeys.KRYO);

    public static Serializer getSerializer(String key){
        return serializerMap.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}
