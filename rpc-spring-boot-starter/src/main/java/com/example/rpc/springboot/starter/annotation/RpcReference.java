package com.example.rpc.springboot.starter.annotation;


import example.rpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {

    Class<?> interfaceClass() default void.class;

    String version() default RpcConstant.DEFAULT_VERSION;
}
