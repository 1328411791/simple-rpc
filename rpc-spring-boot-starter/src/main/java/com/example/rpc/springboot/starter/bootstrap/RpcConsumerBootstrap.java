package com.example.rpc.springboot.starter.bootstrap;

import com.example.rpc.springboot.starter.annotation.RpcReference;
import example.rpc.RpcApplication;
import example.rpc.proxy.ServiceProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class RpcConsumerBootstrap implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        Field[] declaredFields = beanClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            RpcReference annotation = declaredField.getAnnotation(RpcReference.class);
            if (annotation != null) {
                Class<?> interfaceClass = annotation.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = declaredField.getType();
                }
                declaredField.setAccessible(true);
                Object proxy = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    declaredField.set(bean, proxy);
                    declaredField.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("注入失败", e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
