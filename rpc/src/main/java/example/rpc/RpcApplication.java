package example.rpc;

import example.rpc.constant.RpcConstant;
import example.rpc.model.RegistryConfig;
import example.rpc.model.RpcConfig;
import example.rpc.registry.Registry;
import example.rpc.registry.RegistryFactory;
import example.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("RpcApplication.init: " + rpcConfig.toString());
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        registry.initRegistry(registryConfig);
        log.info("RpcApplication.init: registry init success");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("RpcApplication.init: shutdownHook start");
            registry.destroy();
            log.info("RpcApplication.init: shutdownHook end");
        }));
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
