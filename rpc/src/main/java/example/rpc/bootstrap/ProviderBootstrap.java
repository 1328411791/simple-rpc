package example.rpc.bootstrap;

import example.rpc.RpcApplication;
import example.rpc.model.RegistryConfig;
import example.rpc.model.RpcConfig;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.registry.LocalRegistry;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.server.VertxHttpServer;

import java.util.List;

public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList){

        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());


            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
            serviceMetaInfo.setServiceAddress(registryConfig.getAddress());
            serviceMetaInfo.setServicePort(String.valueOf(registryConfig.getPort()));

            // 初始化服务
            registry.register(serviceMetaInfo);

            VertxHttpServer httpServer = new VertxHttpServer();
            httpServer.doStart(rpcConfig.getServerPort());
        }
    }
}
