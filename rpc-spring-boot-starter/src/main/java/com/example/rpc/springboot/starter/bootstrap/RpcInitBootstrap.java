package com.example.rpc.springboot.starter.bootstrap;

import com.example.rpc.springboot.starter.annotation.EnableRpc;
import example.rpc.RpcApplication;
import example.rpc.model.RpcConfig;
import example.rpc.server.VertxHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata
            , BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        boolean needServer = (boolean) importingClassMetadata
                .getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        if(needServer){
            VertxHttpServer vertxHttpServer = new VertxHttpServer();
            vertxHttpServer.doStart(rpcConfig.getServerPort());
        }else{
            log.info("服务端启动已关闭");
        }

    }
}
