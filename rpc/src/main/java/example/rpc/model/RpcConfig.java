package example.rpc.model;

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

    // 注册中心类型
    private String registryType;

    // 服务地址
    private String serverAddress;

    // 服务组
    private String group;

    // 服务端口
    private int serverPort;

    // 用户名
    private String username;

    // 密码
    private String password;

    // Mock
    private boolean mock = false;

    // 序列化器
    private String serializer = SerializerKeys.JDK;

    public RegistryConfig getRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistryType(registryType);
        registryConfig.setPort(serverPort);
        registryConfig.setAddress(serverAddress);
        registryConfig.setUsername(username);
        registryConfig.setPassword(password);
        return registryConfig;
    }
}
