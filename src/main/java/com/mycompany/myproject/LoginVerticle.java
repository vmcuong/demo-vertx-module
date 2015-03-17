package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;

import java.util.Map;

public class LoginVerticle extends Verticle {
    public void start() {
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest request) {
                request.bodyHandler(new Handler<Buffer>() {
                    public void handle(Buffer buffer) {
                        String postData=buffer.toString();
                        request.response().putHeader("content-type", "application/json");
                        request.response().end(postData);
                    }
                });
                        //StringBuilder sb = new StringBuilder();
                        ////request.method();
                        //for (Map.Entry<String, String> header: request.headers().entries()) {
                        //    sb.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
                        //}
                //request.response().putHeader("content-type", "text/plain");
                //request.response().end(request.method());
            //request.response().end(sb.toString());

        }
    }).listen(8080);
    }
}