package example.rpc.spi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import example.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {

    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    // 缓存实例
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    private static String SYSTEM_SPI = "META-INF/rpc/system/";

    private static String CUSTOM_SPI = "META-INF/rpc/custom/";

    private static final List<String> SCAN_DIR = Arrays.asList(SYSTEM_SPI, CUSTOM_SPI);

    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public static void loadAll() {
        log.info("SpiLoader.loadAll");
        for (Class<?> clazz : LOAD_CLASS_LIST) {
            load(clazz);
        }
    }

    public static <T> T getInstance(Class<T> clazz, String key) {
        String tClassName = clazz.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException("未找到对应的SPI: " + tClassName);
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException("未找到对应的SPI: " + tClassName + " key: " + key);
        }
        Class<?> aClass = keyClassMap.get(key);
        String implClassName = aClass.getName();
        // 从缓存中获取实例
        if(!instanceCache.containsKey(implClassName)) {
            // 不在缓存中则创建实例
            try {
                instanceCache.put(implClassName, aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("SpiLoader.getInstance: " + e);
                throw new RuntimeException(e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }


    public static Map<String,Class<?>> load(Class<?> clazz) {
        log.info("SpiLoader.load: " + clazz.getName());
        Map<String, Class<?>> keyClassMap = new ConcurrentHashMap<>();

        // 加载系统SPI
        for (String spi : SCAN_DIR) {
            // 获取目录下所有资源的URL
            List<URL> resources = ResourceUtil.getResources(spi+clazz.getName());

            for(URL resource : resources) {
                try {
                    // 读取目录下的文件内容
                    InputStreamReader reader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        log.info("SpiLoader.load: " + line);
                        String[] split = line.split("=");
                        if(split.length>1){
                            String key = split[0];
                            String value = split[1];
                            Class<?> aClass = Class.forName(value);
                            keyClassMap.put(key, aClass);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    log.error("SpiLoader.load: " + e);
                    throw new RuntimeException(e);
                }
            }
        }
        loaderMap.put(clazz.getName(),keyClassMap);
        return keyClassMap;
    }
}
