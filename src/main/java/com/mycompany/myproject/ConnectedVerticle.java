package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.logging.Logger;
/**
 * Created by Toan on 13/03/2015.
 */
public class ConnectedVerticle extends Verticle {
    DBConnector DBC;

    public ConnectedVerticle()
    {
        DBC=new DBConnector();
    }

    public void start() {
        DBC=new DBConnector();
        final Logger logger = container.logger();
        logger.info(" DB connected ");
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response().headers().set("Content-Type", "text/plain");
                //req.response().end("Hello World TIMO");
                req.response().end(DBC.getUser_JSONData().encodePrettily());
            }
        }).listen(8080);
        //httpServerRequest.response().end(callback + "(" + db.getJSONData().encodePrettily() + ")");
    }
}
