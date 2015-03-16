package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.logging.Logger;

import java.util.Set;

/**
 * Created by Toan on 16/03/2015.
 */
public class GetUserDataVerticle extends Verticle {
    public void start() {

        final Logger logger = container.logger();
        logger.info("-----start getting data ----");
        EventBus eb=vertx.eventBus();


        HttpServer sv=vertx.createHttpServer();
        final JsonObject receivedJS;
        sv.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest req) {

                    JsonObject sendJS=new JsonObject().putString("action", "select")
                            .putString("table" , "tblUserAccess");
                    //.putString("fields" , "[\"UserName\", \"UserFirstName\"],");
                    vertx.eventBus().send("mmsoft.mysql", sendJS, new Handler<Message<JsonObject>>() {
                        public void handle(final Message<JsonObject> message) {
                            req.response().headers().set("Content-Type", "application/json");
                            req.response().end(message.body().encodePrettily());
                        }
                    });

               // req.response().headers().set("Content-Type", "text/plain");

              //  req.response().end("Hello World:" + vertx.sharedData().getSet("mmsoft.userset"));

            }
        });
        long timerID = vertx.setTimer(3000, new Handler<Long>() {
            public void handle(Long timerID) {
                //log.info("And one second later this is printed");
                JsonObject sendJS=new JsonObject().putString("action", "select")
                        .putString("table" , "tblUserAccess");
                        //.putString("fields" , "[\"UserName\", \"UserFirstName\"],");
                vertx.eventBus().send("mmsoft.mysql", sendJS, new Handler<Message<JsonObject>>() {
                    public void handle(final Message<JsonObject> message) {
                        System.out.println("I received a reply ");
                        logger.info("-----reply: " +  message.body().encodePrettily());
                        //eceivedJS=(JsonObject)message;
                        Set<String> set=vertx.sharedData().getSet("mmsoft.userset");
                        String enc=message.body().encodePrettily();
                        set.add(enc);
                    }
                });
            }
        });



        sv.listen(8080);
        /*vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response().headers().set("Content-Type", "text/plain");

                req.response().end(DBC.getUser_JSONData().encodePrettily());
            }
        }).listen(8080);*/
        //httpServerRequest.response().end(callback + "(" + db.getJSONData().encodePrettily() + ")");
    }
}
