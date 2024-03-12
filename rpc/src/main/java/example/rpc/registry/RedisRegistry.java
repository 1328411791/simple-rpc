package example.rpc.registry;


import cn.hutool.json.JSONUtil;
import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisRegistry implements Registry{

    private static final String REGISTRY_PATH = "rpc:";

    private Jedis jedis;

    @Override
    public void initRegistry(RegistryConfig registryConfig) {

        jedis = new Jedis(registryConfig.getAddress(), registryConfig.getPort()
                , Math.toIntExact(registryConfig.getTimeout()));
        // 设置密码
        jedis.auth(registryConfig.getPassword());
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {
        String serviceKey =REGISTRY_PATH + serviceMetaInfo.getServiceNodeKey();

        // 注册服务
        jedis.set(serviceKey, JSONUtil.toJsonStr(serviceMetaInfo));
        // 设置过期时间
        jedis.expire(serviceKey, 30);

    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        String serviceKey =REGISTRY_PATH + serviceMetaInfo.getServiceNodeKey();
        jedis.del(serviceKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        Set<String> keys = jedis.keys(REGISTRY_PATH + serviceKey + "/*");
        List<ServiceMetaInfo> serviceMetaInfos = keys.stream().map(key ->
                JSONUtil.toBean(jedis.get(key), ServiceMetaInfo.class)).toList();
        return serviceMetaInfos;
    }

    @Override
    public void destroy() {
        System.out.println("destroy redis registry");
        if (jedis != null) {
            jedis.close();
        }
    }
}
