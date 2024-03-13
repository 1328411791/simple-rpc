package example.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class ServiceMetaInfo {

        // 服务名称
        private String serviceName;

        // 服务版本
        private String serviceVersion;

        // 服务地址
        private String serviceAddress;

        // 服务端口
        private String servicePort;

        // 服务组
        private String serviceGroup;

        public String getServiceName() {
            return String.format("%s/%s/%s", serviceName, serviceVersion, serviceGroup);
        }

        public String getServiceNodeKey() {
                return String.format("%s/%s", serviceName, serviceGroup);
        }

        public String getServiceAddress() {
                if (!StrUtil.contains(serviceAddress, "http")) {
                    return String.format("http://%s:%s", serviceAddress,servicePort);
                }
            return String.format("%s/%s", serviceAddress, servicePort);
        }
}
