package example.rpc.bootstrap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRegisterInfo<T> {

    // 服务名称
    private String serviceName;

    // 实现类
    private Class<? extends T> implClass;
}
