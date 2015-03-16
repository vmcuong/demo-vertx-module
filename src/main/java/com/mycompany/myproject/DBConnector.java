package com.mycompany.myproject;

/**
 * Created by Toan on 13/03/2015.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.sql.SQLException;


import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import java.sql.*;


public class DBConnector {

    private Connection connection;
    private String dbInformation;
    private String userName;
    private String password;

    public DBConnector() {
        connection = null;
        dbInformation = "jdbc:mysql://52.64.11.136:3306/timo_application_db";
        userName = "admin";
        password = "@dmin123!";

        try {
            //load api
            //Class.forName("org.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(dbInformation, userName, password);
            connection.close();

        } catch (SQLDataException sqlDataException) {
            System.err.print(sqlDataException);
        } catch (SQLException sqlException) {
            System.err.print(sqlException);
        } catch (ClassNotFoundException classNotFound) {
            System.err.print(classNotFound);
        }
    }

    public boolean testConnection() {

        try {
            connection = DriverManager.getConnection(dbInformation, userName, password);
            connection.close();
            return connection.isClosed();
        } catch (SQLException sqlException) {
            return false;
        }
    }
    public JsonObject getUser_JSONData(){

        ResultSet rs = null;

        JsonObject root = new JsonObject();

        JsonObject users = new JsonObject();

        JsonArray user = new JsonArray();
        try {

            connection = DriverManager.getConnection(dbInformation,userName,password);
            Statement st = connection.createStatement();
            rs = st.executeQuery("SELECT * FROM \"tblUserAccess\";");

            while(rs.next()){

                //System.out.println(rs.getString("empID"));
                //System.out.println(rs.getString("empName"));
                //System.out.println(rs.getString("position"));
                //System.out.println(rs.getString("project"));

                JsonObject usr = new JsonObject();
                usr.putString("UserID", rs.getString("UserID"));
                usr.putString("UserName", rs.getString("UserName"));
                usr.putString("UserFirstName", rs.getString("UserFirstName"));


                user.addElement(usr);
            }
            users.putArray("user", user);

            root.putObject("users", users);

            connection.close();
        }catch (SQLDataException sqlDataException){
            System.err.print(sqlDataException);
        }catch (SQLException sqlException){
            System.err.print(sqlException);
        }
        return root;
    }
}