package example.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import example.rpc.RpcApplication;
import example.rpc.model.*;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.serializer.JdkSerializer;
import example.rpc.serializer.Serializer;
import example.rpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取序列化器
        final Serializer serializer = SerializerFactory
                .getSerializer(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();

        RpcRequest request = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        try{
            byte[] bytes = serializer.serialize(request);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            // 获取注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
            serviceMetaInfo.setServiceAddress(registryConfig.getAddress());
            serviceMetaInfo.setServicePort(String.valueOf(registryConfig.getPort()));

            // 从服务注册中心获取服务地址
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceNodeKey());

            if (CollUtil.isEmpty(serviceMetaInfos)){
                throw new RuntimeException("service not found");
            }
            ServiceMetaInfo getServiceMetaInfo = serviceMetaInfos.get(0);

            try(HttpResponse response = HttpUtil.createPost(getServiceMetaInfo.getServiceAddress())
                    .body(bytes).execute()){
                byte[] responseBytes = response.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(responseBytes, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
