package com.example.rpc.springboot.starter.bootstrap;

import com.example.rpc.springboot.starter.annotation.RpcService;
import example.rpc.RpcApplication;
import example.rpc.model.RegistryConfig;
import example.rpc.model.RpcConfig;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.registry.LocalRegistry;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.server.VertxHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    // Bean初始化之后，将服务注册到注册中心
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 注册服务
            Class<?> interfaceClass = rpcService.interfaceClass();

            // 如果没有指定接口，则获取第一个接口
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            LocalRegistry.register(serviceName, beanClass);

            log.info("服务注册成功，服务名称：{}，版本：{}", serviceName, serviceVersion);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceGroup(rpcConfig.getGroup());
            serviceMetaInfo.setServiceAddress(registryConfig.getAddress());
            serviceMetaInfo.setServicePort(String.valueOf(registryConfig.getPort()));

            // 初始化服务
            try {
                registry.register(serviceMetaInfo);
            }
            catch (Exception e) {
                log.error("服务注册失败", e);
                throw new RuntimeException("服务注册失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
