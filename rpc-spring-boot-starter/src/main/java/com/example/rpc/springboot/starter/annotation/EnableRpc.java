package com.example.rpc.springboot.starter.annotation;

import com.example.rpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.example.rpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.example.rpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    // 服务启动
    boolean needServer() default true;

}
