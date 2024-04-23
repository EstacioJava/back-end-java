package com.estacio.demo.controller;

import java.sql.*;

public class Database {
   public static void connect () {
      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         System.out.println("[SQLITE] Successfully connected to the database.");
      } catch (SQLException error) {
         System.out.println(error.getMessage());
      }
   }

   public static String getUsers () {
      String responseStr = "";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         stmt.execute("CREATE TABLE IF NOT EXISTS USERS(ID INTEGER, NAME VARCHAR)");
         stmt.execute("INSERT INTO USERS( ID, NAME) VALUES (1, 'Wolmir'), (2, 'Garbin')");
         ResultSet allUsers = connection.prepareStatement("SELECT * FROM USERS").executeQuery();

         while (allUsers.next()) {
            responseStr = String.format("%s\nID: %d / Name: %s", responseStr, allUsers.getInt("ID"), allUsers.getString("NAME"));
            // System.out.println("ID: " + allUsers.getInt("ID") + " / NAME: " + allUsers.getString("NAME"));
         }

         return responseStr;
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "SQLException";
      }
   }
}
