package com.example.ik.myapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {
    private static String username;
    private static String password;
    private static String mysql;
    public static Connection dbconnection() throws SQLException {
        mysql="jdbc:mysql://10.0.2.2:3306/enterprise";
        username="root";
        password="borni2019";
        return DriverManager.getConnection(mysql,username,password);
    }
}
