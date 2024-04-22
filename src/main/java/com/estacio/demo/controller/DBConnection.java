package com.estacio.demo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.protocol.Resultset;

public class DBConnection {
   Connection connection = null;

   try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_test_01", "user", "password");
      ResultSet rsClient = connection.createStatement().executeQuery("SELECT * FROM Clients");
      while (rsClient.next()) {
         System.out.println("Name: " + rsClient.getString("name"));
      }
   } catch (ClassNotFoundException classError) {
      System.out.println("MySQL class not found.");
   } catch (SQLException sqlError) {
      System.out.println("Database error: " + sqlError.getMessage());
   } finally {
      if (connection != null) {
         connection.close();
      }
   }
}
