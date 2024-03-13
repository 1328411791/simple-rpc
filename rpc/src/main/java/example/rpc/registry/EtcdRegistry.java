package example.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class EtcdRegistry  implements Registry{

    private static final String REGISTRY_TYPE = "/rpc/";

    private final Set<String> localRegistryNode = new HashSet<>();

    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    private Client client;

    private KV kvClient;


    @Override
    public void initRegistry(RegistryConfig registryConfig) {
        // 创建链接客户端
        client = Client.builder().endpoints(registryConfig.getAddress()+":"+registryConfig.getPort())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();

        // 心跳检测
        headBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {

        log.info("register service:{}", serviceMetaInfo);
        Lease leaseClient = client.getLeaseClient();

        // 注册服务，设置租约时间为30s
        long leaseId = leaseClient.grant(500).join().getID();

        String registryKey = REGISTRY_TYPE + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey.getBytes());
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo).getBytes());

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

        kvClient.put(key, value, putOption).join();

        // 保存注册节点
        localRegistryNode.add(registryKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        String registryKey = REGISTRY_TYPE + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8));

        localRegistryNode.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        List<ServiceMetaInfo> cacheServiceMetaInfos = registryServiceCache.readCache();
        if (CollUtil.isNotEmpty(cacheServiceMetaInfos)) {
            return cacheServiceMetaInfos;
        }

        String searchPrefix = REGISTRY_TYPE + serviceKey+"/";
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                    getOption).join().getKvs();

            List<ServiceMetaInfo> serviceMetaInfos = keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());

            registryServiceCache.writeCache(serviceMetaInfos);

            return serviceMetaInfos;
        }catch (Exception e) {
            throw new RuntimeException("service discovery error", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy etcd registry");

        for (String registryNode : localRegistryNode) {
            try {
                kvClient.delete(ByteSequence.from(registryNode, StandardCharsets.UTF_8));
            }catch (Exception e) {
                log.error("destroy error", e);
            }
        }

        if (client != null) {
            client.close();
        }
        if (kvClient != null) {
            kvClient.close();
        }
    }

    @Override
    public void headBeat() {
        // 定时发送心跳
        CronUtil.schedule("*/10 * * * * *", (Task) () -> {
            log.info("headBeat {}" , System.currentTimeMillis());
            try {
                // 获取本节点所有的key
                for (String registryNode : localRegistryNode) {

                    log.info("headBeat registryNode:{}", registryNode);

                    List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(registryNode, StandardCharsets.UTF_8)).join().getKvs();

                    if (CollUtil.isEmpty(keyValues)) {
                        // 重新注册
                        continue;
                    }

                    KeyValue keyValue = keyValues.get(0);
                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    register(serviceMetaInfo);
                }
            }catch (Exception e){
                log.error("headBeat error", e);
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}


