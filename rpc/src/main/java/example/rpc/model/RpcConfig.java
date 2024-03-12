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

    // 服务地址
    private String serverAddress;

    // 服务组
    private String group;

    // 服务端口
    private int serverPort;

    // Mock
    private boolean mock = false;

    // 序列化器
    private String serializer = SerializerKeys.JDK;
}
