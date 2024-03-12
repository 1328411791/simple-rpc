package example.rpc.proxy;

import example.rpc.RpcApplication;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {

        if(RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new ServiceProxy()
        );
    }

    // 获取mock代理
    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new MockServiceProxy()
        );
    }
}
