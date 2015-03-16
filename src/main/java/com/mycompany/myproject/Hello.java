package com.mycompany.myproject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

//import org.vertx.java.platform.impl.cli.Starter;
import java.util.Map;
public class Hello extends Verticle {
    public void start() {
    vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
        public void handle(HttpServerRequest req) {
            req.response().headers().set("Content-Type", "text/plain");
            req.response().end("Hello World TIMO");
        }
    }).listen(8080);
    }
}