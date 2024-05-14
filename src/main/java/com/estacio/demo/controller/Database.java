package com.estacio.demo.controller;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Database {
   public static String connect () {
      JSONObject response = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         System.out.println("[SQLITE] Successfully connected to the database.");
         
         Statement stmt = connection.createStatement();
         
         stmt.execute(
            "CREATE TABLE IF NOT EXISTS storage (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), price REAL, length INTEGER, width INTEGER, quantity INTEGER)"
         );

         stmt.execute(
            "CREATE TABLE IF NOT EXISTS materials (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), thickness INTEGER)"
         );

         response.put("message", "Successfully connected to the database.");
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }
}
