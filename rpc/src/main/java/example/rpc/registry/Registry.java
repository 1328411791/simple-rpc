package example.rpc.registry;

import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {

    void initRegistry(RegistryConfig registryConfig);

    void register(ServiceMetaInfo serviceMetaInfo);

    void unregister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void destroy();
}
