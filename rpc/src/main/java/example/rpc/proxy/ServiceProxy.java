package example.rpc.proxy;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import example.rpc.model.RpcRequest;
import example.rpc.model.RpcResponse;
import example.rpc.serializer.JdkSerializer;
import example.rpc.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JdkSerializer();

        RpcRequest request = RpcRequest.builder()
                .serviceName(proxy.getClass().getInterfaces()[0].getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        try{
            byte[] bytes = serializer.serialize(request);

            try(HttpResponse response = HttpUtil.createPost("http://localhost:5050")
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
