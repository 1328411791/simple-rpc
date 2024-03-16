package example.provider;

import example.common.service.UserService;
import example.provider.servicer.UserServiceImpl;
import example.rpc.RpcApplication;
import example.rpc.bootstrap.ProviderBootstrap;
import example.rpc.bootstrap.ServiceRegisterInfo;
import example.rpc.model.RegistryConfig;
import example.rpc.model.RpcConfig;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.registry.LocalRegistry;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.server.HttpServer;
import example.rpc.server.VertxHttpServer;

import java.util.ArrayList;
import java.util.List;

public class ProviderStart {
    public static void main(String[] args) {

        List<ServiceRegisterInfo <?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo =
                new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
