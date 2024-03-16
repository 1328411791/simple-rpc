package example.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import example.rpc.RpcApplication;
import example.rpc.fault.retry.RetryStrategy;
import example.rpc.fault.retry.RetryStrategyFactory;
import example.rpc.fault.tolerant.TolerantStrategy;
import example.rpc.fault.tolerant.TolerantStrategyFactory;
import example.rpc.loadbalancer.LoadBalancer;
import example.rpc.loadbalancer.LoadBalancerFactory;
import example.rpc.model.*;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.serializer.JdkSerializer;
import example.rpc.serializer.Serializer;
import example.rpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RpcResponse rpcResponse = null;
        // 服务元信息
        List<ServiceMetaInfo> serviceMetaInfos = null;
        ServiceMetaInfo selectedService = null;
        byte[] bytes = serializer.serialize(request);

        try{
            // 获取注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
            serviceMetaInfo.setServiceAddress(registryConfig.getAddress());
            serviceMetaInfo.setServicePort(String.valueOf(registryConfig.getPort()));

            String serviceNodeKey = serviceMetaInfo.getServiceNodeKey();
            log.info("serviceNodeKey:{}", serviceNodeKey);
            // 从服务注册中心获取服务地址
            serviceMetaInfos = registry.serviceDiscovery(serviceNodeKey);

            if (CollUtil.isEmpty(serviceMetaInfos)){
                throw new RuntimeException("service not found");
            }

            // 获取负载均衡器
            LoadBalancer instance = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", request.getMethodName());
            selectedService = instance.select(requestParams, serviceMetaInfos);

            String serviceUrl = selectedService.getServiceUrl();
            //
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetry());
             rpcResponse = retryStrategy.doRetry(() -> {
                HttpResponse response = HttpUtil.createPost(serviceUrl)
                        .body(bytes)
                        .execute();
                byte[] bodyBytes = response.bodyBytes();
                return serializer.deserialize(bodyBytes, RpcResponse.class);
            });

        } catch (Exception e) {
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerant());
            Map<String,Object> context = new HashMap<>();
            context.put("ServiceMetaInfos",serviceMetaInfos);
            context.put("selectService",selectedService);
            context.put("request",request);
            context.put("rpcConfig",rpcConfig);
            context.put("bytes",bytes);
            tolerantStrategy.doTolerant(context, e);
        }

        return rpcResponse.getData();
    }
}
