package example.provider;

import example.provider.servicer.UserServiceImpl;
import example.rpc.registry.LocalRegistry;
import example.rpc.server.HttpServer;
import example.rpc.server.VertxHttpServer;

public class ProviderStart {
    public static void main(String[] args) {

        LocalRegistry.register("example.common.service.UserService", UserServiceImpl.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(5050);
    }
}
