package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
//import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
//import org.vertx.java.core.json.impl.Json;
import org.vertx.java.platform.Verticle;
//import org.vertx.scala.core.json.Json$;
//import sun.org.mozilla.javascript.internal.json.JsonParser;

//import java.util.Map;
import java.net.URLDecoder;
import java.util.Random;

public class LoginVerticle extends Verticle {
    public void start() {

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(
                //new RouteMatcher().get("/login",
                new Handler<HttpServerRequest>() {
                    public void handle(final HttpServerRequest request) {
                        request.bodyHandler(new Handler<Buffer>() {
                                                public void handle(Buffer buffer) {
                                                    String postData= URLDecoder.decode(buffer.toString());
                                                    //QueryStringDecoder qsd=new QueryStringDecoder(buffer.toString(),false);
                                                    //URLDecoder.decode(buffer.toString(),)
                                                    //String un=request.formAttributes().get("username");
                                                    // request.response().putHeader("content-type", "application/json");
                                                    // request.response().end(un);

                                                    //String postData = "username=richard@gmails.com&password=connect";
                                                    //extract value to particular key
                                                    boolean validData=false;
                                                    String[] data;
                                                    String[] usernameItem;
                                                    String[] passwordItem;
                                                    try {
                                                        data = postData.split("&");
                                                        String[] item1=usernameItem = data[0].split("=");
                                                        String[] item2=passwordItem = data[1].split("=");
                                                        if (item1[0].equals("username")){
                                                            usernameItem=item1;
                                                        }
                                                        else if (item1[0].equals("password")){
                                                            passwordItem=item1;
                                                        }
                                                        if (item2[0].equals("username")){
                                                            usernameItem=item2;
                                                        }
                                                        else if (item2[0].equals("password")){
                                                            passwordItem=item2;
                                                        }
                                                        validData=true;
                                                    }catch (Exception ex){
                                                        validData=false;
                                                        request.response().putHeader("content-type", "application/json");
                                                        request.response().end("No valid data");
                                                        return;
                                                    }
                                                    final String usernameValue = usernameItem[1];
                                                    String passwordValue = passwordItem[1];
                                                    //encrypt password
                                                    final String receivedEncryptedPassword = MD5Gen.EncryptString(passwordValue);
                                                    //compare database
                                                    final JsonObject sendJS = new JsonObject()
                                                            .putString("action", "prepared")
                                                            .putString("statement", "SELECT  UserId,UserEmail, Password FROM tblUserAccess WHERE UserEmail=? AND Password=?")
                                                            .putArray("values", new JsonArray().addString(usernameValue).addString(receivedEncryptedPassword));
                        /*
                        vertx.eventBus().send("mmsoft.mysql", sendJS, new Handler<Message<JsonObject>>() {
                            public void handle(final Message<JsonObject> message) {
                                request.response().putHeader("content-type", "application/json");
                                request.response().end(sendJS.encodePrettily()+" - "+ message.body().encodePrettily());
                            }
                        });
                        */

                                                    vertx.eventBus().send("mmsoft.mysql", sendJS, new Handler<Message<JsonObject>>() {
                                                        public void handle(final Message<JsonObject> message) {
                                                            String returnToken = "";
                                                            String userId="";
                                                            int messageCode=401;
                                                            String description="Unauthorized";
                                                            int row=message.body().getInteger("rows");
                                                            if (row>0) {
                                                                JsonArray ja = message.body().getArray("results");
                                                                userId=((JsonArray) ja.get(0)).get(0).toString();
                                                                String dbEncryptedPass = ((JsonArray) ja.get(0)).get(2).toString();
                                                                boolean passEqual = false;
                                                                if (receivedEncryptedPassword.equals(dbEncryptedPass)) {
                                                                    passEqual = true;
                                                                }
                                                                //generate token if login succeed


                                                                if (passEqual) {
                                                                    returnToken = usernameValue + (new Random()).toString();
                                                                    returnToken = MD5Gen.EncryptString(returnToken);
                                                                    messageCode = 200;
                                                                    description = "ok";
                                                                }
                                    /*else {
                                        messageCode = 401;
                                        description = "Unauthorized";
                                    }
                                    */
                                                            }
                                                            //Write token to database

                                                            final JsonObject writeJS = new JsonObject()
                                                                    .putString("action", "prepared")
                                                                    .putString("statement", "INSERT INTO tblTokenLogin(UserId,LoginToken,CreateDate,ExpireDate,LastDateActive) Values(?,?,now(),DATE_ADD(now(),INTERVAL 30 MINUTE),now())")
                                                                    .putArray("values", new JsonArray().addString(userId).addString(returnToken));
                                /*
                                        .putString("action", "insert")
                                        .putString("table", "tblTokenLogin")
                                        .putArray("fields", new JsonArray().addString("UserId").addString("LoginToken").addString("CreateDate"))
                                        .putArray("values", new JsonArray().addArray(new JsonArray().addString(userId).addString(returnToken).addString("now()")));
                                        */
                                                            vertx.eventBus().send("mmsoft.mysql.token",writeJS, new Handler<Message<JsonObject>>(){
                                                                @Override
                                                                public void handle(Message<JsonObject> jsonObjectMessage) {
                                                                    // request.response().putHeader("content-type", "application/json");
                                                                    // request.response().end(writeJS.encodePrettily() + " - " + jsonObjectMessage.body().encodePrettily());
                                                                }
                                                            });
                                                            //Return token to client
                                                            JsonObject returnJS = new JsonObject();
                                                            returnJS.putString("user_name", usernameValue);
                                                            returnJS.putString("token", returnToken);
                                                            returnJS.putNumber("message_code", messageCode);
                                                            returnJS.putString("description", description);

                                                            request.response().putHeader("content-type", "application/json");
                                                            //request.response().end(dbEncryptedPass+" - "+receivedEncryptedPassword+" -equal:"+passEqual +" -token: "+returnToken);
                                                            request.response().end(returnJS.encodePrettily());
                                                            //request.response().end(message.body().encodePrettily());
                                                        }
                                                    });


                                                }
                                            }

                        );


                    }
                }
                //)
        ).listen(8080);
    }
}
