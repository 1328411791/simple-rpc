package example.Comsumer;

import example.common.model.User;
import example.common.service.UserService;
import example.rpc.RpcApplication;
import example.rpc.model.RpcConfig;
import example.rpc.proxy.ServiceProxy;
import example.rpc.proxy.ServiceProxyFactory;

public class ConsumerStart {
    public static void main(String[] args) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println("ConsumerStart.main: " + rpcConfig.toString());

        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        userService.getUser(user);
        System.out.println("ConsumerStart.main: " + user);
    }
}
