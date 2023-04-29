package com.example.ecommerce;
import java.sql.*;

public class DatabaseConnection {

    // moving this to application.properties file.
    String dbURL = "jdbc:mysql://localhost:3306/ecomm";  // application.dbURL
    String userName = "root"; // application.userName
    String password = "Kishan@123"; // application.password

    public boolean insertUpdate(String query){
        Statement statement = getStatement();
        try {
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getQueryTable(String query){
        Statement statement = getStatement();
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Statement getStatement(){
        try{
            Connection conn = DriverManager.getConnection(dbURL, userName,password);
            return conn.createStatement();
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    // order -> columns & rows -> QueryTool -> query statement - "select * from order;"

//    public static void main(String[] args) {
//        String query = "SELECT * FROM products";
//        DatabaseConnection dbConn = new DatabaseConnection();
//        ResultSet rs = dbConn.getQueryTable(query);
//        if(rs != null){
//            System.out.println("Connected to Database");
//        }
//    }
}
