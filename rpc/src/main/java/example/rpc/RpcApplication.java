package example.rpc;

import example.rpc.constant.RpcConstant;
import example.rpc.model.RpcConfig;
import example.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig rpcConfig) {
        RpcApplication.rpcConfig = rpcConfig;
        log.info("RpcApplication.init: " + rpcConfig.toString());
    }

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            log.error("RpcApplication.init: loadConfig error", e);
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
