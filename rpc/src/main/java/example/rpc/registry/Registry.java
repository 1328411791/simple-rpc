package example.rpc.registry;

import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {

    // 初始化注册中心
    void initRegistry(RegistryConfig registryConfig);

    // 注册服务
    void register(ServiceMetaInfo serviceMetaInfo);

    // 注销服务
    void unregister(ServiceMetaInfo serviceMetaInfo);

    // 服务发现
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    // 销毁注册中心
    void destroy();
}
