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
        // 初始化注册中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        // 初始化服务
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerAddress());
        serviceMetaInfo.setServicePort(String.valueOf(rpcConfig.getServerPort()));
        System.out.println("ProviderStart.main: registry init success");
        registry.register(serviceMetaInfo);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(5050);
    }
}
