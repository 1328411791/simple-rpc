package example.rpc.registry;

import example.rpc.model.ServiceMetaInfo;

import java.util.List;

public class RegistryServiceCache {

    List<ServiceMetaInfo> serviceCache;

    public void writeCache(List<ServiceMetaInfo> newServiceMetaInfos) {
        serviceCache = newServiceMetaInfos;
    }

    List<ServiceMetaInfo> readCache() {
        return serviceCache;
    }

    void clearCache() {
        serviceCache = null;
    }
}
