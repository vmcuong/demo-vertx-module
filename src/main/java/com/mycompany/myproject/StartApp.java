package com.mycompany.myproject;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.platform.Verticle;

/**
 * Created by Toan on 16/03/2015.
 */
public class StartApp extends Verticle {
    public void start() {
        container.deployVerticle("com.mycompany.myproject.ModMySQLConnector");
        /*,new AsyncResultHandler<String>() {
            public void handle(AsyncResult<String> asyncResult) {
                if (asyncResult.succeeded()) {
                    System.out.println("The MySQL verticle has been deployed, deployment ID is " + asyncResult.result());
                } else {
                    asyncResult.cause().printStackTrace();
                }
            }
        });*/
       // container.deployVerticle("com.mycompany.myproject.GetUserDataVerticle");
        container.deployVerticle("com.mycompany.myproject.LoginVerticle");
    }
}
