package example.Comsumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import example.common.model.User;
import example.common.service.UserService;
import example.rpc.model.RpcRequest;
import example.rpc.model.RpcResponse;
import example.rpc.serializer.JdkSerializer;
import example.rpc.serializer.Serializer;

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameters(new Object[]{user})
                .parameterTypes(new Class[]{User.class})
                .build();

        try {
            // 构造发送和接收的数据
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes).execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
            // send request to server
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
