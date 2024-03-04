package example.rpc.server;


import example.rpc.model.RpcRequest;
import example.rpc.model.RpcResponse;
import example.rpc.registry.LocalRegistry;
import example.rpc.serializer.JdkSerializer;
import example.rpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        final Serializer serializer = new JdkSerializer();

        System.out.println("HttpServerHandler.handle: " + httpServerRequest.path());

        httpServerRequest.bodyHandler(body->{
           byte[] bytes = body.getBytes();
            RpcRequest request = null;
            try {
                request = serializer.deserialize(bytes, RpcRequest.class);
                System.out.println("HttpServerHandler.handle: " + request);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (request == null)
            {
                rpcResponse.setMessage("Request is null");
                doResponse(httpServerRequest, serializer, rpcResponse);
                return;
            }
            try {
                // 获取返回的类信息
                Class<?> clazz = LocalRegistry.getService(request.getServiceName());
                Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
                Object result = method.invoke(clazz.newInstance(), request.getParameters());
                rpcResponse.setData(result);
                rpcResponse.setDataType(result.getClass());
                rpcResponse.setMessage("Success");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            doResponse(httpServerRequest, serializer, rpcResponse);
        });

    }

     private static void doResponse(HttpServerRequest httpServerRequest, Serializer serializer, RpcResponse rpcResponse) {
        HttpServerResponse httpServerResponse = httpServerRequest.response();
        httpServerResponse.putHeader("content-type", "application/json");
        try {
            // set response
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
