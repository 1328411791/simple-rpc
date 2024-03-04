package example.provider;

import example.rpc.server.HttpServer;
import example.rpc.server.impl.VertxHttpServer;

public class ProviderStart {
    public static void main(String[] args) {
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(5050);
    }
}
