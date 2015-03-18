package com.mycompany.myproject;


import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * Created by Toan on 13/03/2015.
 */
public class ModMySQLConnector extends Verticle {
    public void start() {
       /* JsonObject conf=new JsonObject();
        conf.putString("address" , "mmsoft.mysql");
        conf.putString("connection", "MySQL");
        conf.putString("host" , "localhost");
        conf.putNumber("port", 3306);
        conf.putNumber("maxPoolSize", 10);
        conf.putString("username", "root");
        //conf.putString("password","");*/
        JsonObject conf=new JsonObject();
        conf.putString("address" , "mmsoft.mysql");
        conf.putString("connection", "MySQL");
        conf.putString("host" , "52.64.11.136");
        conf.putNumber("port", 3306);
        conf.putNumber("maxPoolSize", 10);
        conf.putString("username", "admin");
        conf.putString("password","@dmin123!");
        conf.putString("database","timo_application_db");
/*                {

                "address" : <event-bus-address-to-listen-on>,
                "connection" : <MySQL|PostgreSQL>,
                "host" : <your-host>,
                "port" : <your-port>,
                "maxPoolSize" : <maximum-number-of-open-connections>,
                "username" : <your-username>,
                "password" : <your-password>,
                "database" : <name-of-your-database>
        }*/
        container.deployModule("io.vertx~mod-mysql-postgresql_2.11~0.3.1", conf,1 );
        //
        JsonObject confAuth=new JsonObject();
        confAuth.putString("address" , "mmsoft.mysql.token");
        confAuth.putString("connection", "MySQL");
        confAuth.putString("host" , "52.64.11.136");
        confAuth.putNumber("port", 3306);
        confAuth.putNumber("maxPoolSize", 10);
        confAuth.putString("username", "admin");
        confAuth.putString("password","@dmin123!");
        confAuth.putString("database","timo_token_db");

        container.deployModule("io.vertx~mod-mysql-postgresql_2.11~0.3.1", confAuth,1 );
    }
}
