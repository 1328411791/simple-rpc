import example.rpc.model.RegistryConfig;
import example.rpc.model.ServiceMetaInfo;
import example.rpc.registry.EtcdRegistry;
import example.rpc.registry.RedisRegistry;
import example.rpc.registry.Registry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RegistryTest {

    final Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://127.0.0.1");
        registryConfig.setPort(2379);
        registryConfig.setRegistryType("etcd");
        registry.initRegistry(registryConfig);
    }

    @Test
    public void registryTest() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceAddress("localhost");
        serviceMetaInfo.setServicePort("8080");
        registry.register(serviceMetaInfo);
    }

    /*
    @Test
    public void unRegistryTest() {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceAddress("localhost");
        serviceMetaInfo.setServicePort("8080");
        registry.unregister(serviceMetaInfo);
    }

     */

    @Test
    public void serviceDiscoveryTest() {
        List<ServiceMetaInfo> serviceMetaInfos =
                registry.serviceDiscovery("myService");
        System.out.println(serviceMetaInfos);
    }

    @Test
    public void headBeatTest() throws InterruptedException {
        registryTest();
        Thread.sleep(1000*60);
    }
}
