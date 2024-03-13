package example.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistryConfig {

    // 注册中心类型
    private String registryType;

    // 注册中心地址
    private String address;

    // 注册中心端口
    private int port;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 超时时间
    private Long timeout = 30L;


    public RegistryConfig(String registryType, String address, int port, String username, String password, Long timeout) {
        this.registryType = registryType;
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }
    public RegistryConfig(String registryType, String address, int port) {
        this.registryType = registryType;
        this.address = address;
        this.port = port;
        this.timeout = timeout;
    }
}
