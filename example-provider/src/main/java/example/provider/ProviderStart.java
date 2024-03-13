package example.provider;

import example.common.service.UserService;
import example.provider.servicer.UserServiceImpl;
import example.rpc.RpcApplication;
import example.rpc.model.RegistryConfig;
import example.rpc.model.RpcConfig;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.registry.LocalRegistry;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.server.HttpServer;
import example.rpc.server.VertxHttpServer;

public class ProviderStart {
    public static void main(String[] args) {

        RpcApplication.init();

        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
        serviceMetaInfo.setServiceAddress(registryConfig.getAddress());
        serviceMetaInfo.setServicePort(String.valueOf(registryConfig.getPort()));
        registry.initRegistry(registryConfig);
        registry.register(serviceMetaInfo);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(5050);
    }
}
