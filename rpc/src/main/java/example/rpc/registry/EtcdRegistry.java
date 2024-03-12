package example.rpc.registry;

import cn.hutool.json.JSONUtil;
import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class EtcdRegistry  implements Registry{

    private static final String REGISTRY_TYPE = "/rpc/";

    private Client client;

    private KV kvClient;


    @Override
    public void initRegistry(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {
        Lease leaseClient = client.getLeaseClient();

        long leaseId = leaseClient.grant(30).join().getID();

        String registryKey = REGISTRY_TYPE + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey.getBytes());
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo).getBytes());

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

        kvClient.put(key, value, putOption).join();
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        String registryKey = REGISTRY_TYPE + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        String searchPrefix = REGISTRY_TYPE + serviceKey+"/";
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                    getOption).join().getKvs();

            return keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        }catch (Exception e) {
            throw new RuntimeException("service discovery error", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy etcd registry");

        if (client != null) {
            client.close();
        }
        if (kvClient != null) {
            kvClient.close();
        }

    }
}
