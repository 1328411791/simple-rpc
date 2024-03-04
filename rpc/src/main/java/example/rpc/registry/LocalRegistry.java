package example.rpc.registry;

import java.util.HashMap;
import java.util.Map;

public class LocalRegistry{

    public static Map<String, Class<?>> map = new HashMap<>();

    public static void register(String serviceName, Class<?> serviceImplClass) {
        // register service
        map.put(serviceName, serviceImplClass);
    }

    public static Class<?> getService(String serviceName){
        return map.get(serviceName);
    }

    public static void unregister(String serviceName){
        // unregister service
        map.remove(serviceName);
    }
}
