package com.example.rpc.springboot.starter.annotation;

import example.rpc.constant.RpcConstant;
import example.rpc.fault.retry.RetryStrategyKeys;
import example.rpc.fault.tolerant.TolerantStrategyKeys;
import example.rpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RpcService {

    // 服务接口
    Class<?> interfaceClass() default void.class;

    // 服务版本
    String serviceVersion() default RpcConstant.DEFAULT_VERSION;

    // 负载均衡策略
    String loadBalance() default LoadBalancerKeys.ROUND_ROBIN;

    // 重试策略
    String registryType() default RetryStrategyKeys.NO;

    // 容错策略
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    boolean mock() default false;
}
