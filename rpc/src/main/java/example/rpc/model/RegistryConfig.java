package example.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistryConfig {

    private String registryType;

    private String address;

    private int port;

    private String username;

    private String password;

    private Long timeout = 5000L;


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
