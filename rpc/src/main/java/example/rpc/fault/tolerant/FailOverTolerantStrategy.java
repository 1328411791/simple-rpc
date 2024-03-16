package example.rpc.fault.tolerant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import example.rpc.RpcApplication;
import example.rpc.fault.retry.RetryStrategy;
import example.rpc.fault.retry.RetryStrategyFactory;
import example.rpc.loadbalancer.LoadBalancer;
import example.rpc.loadbalancer.LoadBalancerFactory;
import example.rpc.model.RpcConfig;
import example.rpc.model.RpcRequest;
import example.rpc.model.RpcResponse;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.serializer.Serializer;
import example.rpc.serializer.SerializerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        List<ServiceMetaInfo> serviceMetaInfos = (List<ServiceMetaInfo>)context.get("ServiceMetaInfos");
        ServiceMetaInfo selectedService = (ServiceMetaInfo)context.get("SelectedService");
        if(CollUtil.isEmpty(serviceMetaInfos)){
            throw new RuntimeException("no available service");
        }
        serviceMetaInfos.remove(selectedService);

        RpcConfig rpcConfig = (RpcConfig)context.get("rpcConfig");
        RpcRequest request = (RpcRequest)context.get("request");
        LoadBalancer instance = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", request.getMethodName());
        ServiceMetaInfo select = instance.select(requestParams, serviceMetaInfos);

        byte[] bytes = (byte[])context.get("bytes");

        final Serializer serializer = SerializerFactory
                .getSerializer(RpcApplication.getRpcConfig().getSerializer());
        RpcResponse rpcResponse = null;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetry());
            rpcResponse = retryStrategy.doRetry(() -> {
                HttpResponse response = HttpUtil.createPost(selectedService.getServiceAddress())
                        .body(bytes)
                        .execute();
                byte[] bodyBytes = response.bodyBytes();
                return serializer.deserialize(bodyBytes, RpcResponse.class);
            });
        }catch (Exception ex){
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerant());
            Map<String,Object> tolerantContext = new HashMap<>();
            tolerantContext.put("ServiceMetaInfos",serviceMetaInfos);
            tolerantContext.put("SelectedService",select);
            tolerantContext.put("request",request);
            tolerantContext.put("rpcConfig",rpcConfig);
            tolerantContext.put("bytes",bytes);
            tolerantStrategy.doTolerant(tolerantContext, ex);
        }

        return rpcResponse;
    }
}
