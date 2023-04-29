package com.example.ecommerce;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    private static byte[] getSha(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getEncryptedPassword(String password){
        try {
            BigInteger num = new BigInteger(1,getSha(password));
            StringBuilder hexString = new StringBuilder(num.toString(16));
            return hexString.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Customer customerLogin(String userEmail, String password) throws SQLException{
        String encryptedPass = getEncryptedPassword(password);
        String query = "SELECT * FROM customer WHERE email = '" + userEmail+"' and password= '"+encryptedPass+"'";
        DatabaseConnection dbConn = new DatabaseConnection();
        try {
            ResultSet rs = dbConn.getQueryTable(query);

            if (rs != null && rs.next()) {
                return new Customer(rs.getInt("cid"),
                        rs.getString("name"),
                        rs.getString("email")
                        );
            }
        }catch (Exception e){
            e.printStackTrace();
            }
        return null;
    }

//    public static void main(String[] args) throws SQLException {
//      System.out.println(customerLogin("kishan@gmail.com","abc"));
//        System.out.println(getEncryptedPassword("abc"));
//    }
}
