package example.rpc.model;

import lombok.Data;

@Data
public class RegistryConfig {

    private String registryType;

    private String address;

    private String username;

    private String password;

    private Long timeout = 5000L;
}
