package example.rpc.server.impl;

import example.rpc.server.HttpServer;
import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port){
        System.out.println("VertxHttpServer.doStart: " + port);

        io.vertx.core.http.HttpServer server = Vertx.vertx().createHttpServer();

        server.requestHandler(request -> {
            System.out.println("VertxHttpServer.doStart: " + request.path());

            request.response().putHeader("content-type", "text/plain").end("Hello World!");
        });

        server.listen(port,httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()) {
                System.out.println("VertxHttpServer.doStart: " + "HTTP server started on port " + port);
            } else {
                System.out.println("VertxHttpServer.doStart: " + "HTTP server failed to start");
            }
            Throwable cause = httpServerAsyncResult.cause();
            if (cause != null) {
                cause.printStackTrace();
            }
        });
    }
}
