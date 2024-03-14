package example.rpc.model;

import example.rpc.loadbalancer.LoadBalancerKeys;
import example.rpc.serializer.SerializerKeys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcConfig {

    // 服务名称
    private String name;

    // 服务版本
    private String version;

    // 服务地址
    private String serverAddress;

    // 服务端口
    private int serverPort;

    // 服务组
    private String group;

    // 注册中心类型
    private String registryType;

    // 注册中心地址
    private String registryAddress;

    // 注册中心端口
    private int registryPort;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 超时时间
    private long timeout = 3000L;

    // Mock
    private boolean mock = false;

    // 序列化器
    private String serializer = SerializerKeys.JDK;

    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;



    public RegistryConfig getRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistryType(registryType);
        registryConfig.setPort(registryPort);
        registryConfig.setAddress(registryAddress);
        registryConfig.setUsername(username);
        registryConfig.setPassword(password);
        registryConfig.setTimeout(timeout);
        return registryConfig;
    }
}
