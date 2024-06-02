package com.estacio.demo.controller;

import java.sql.*;
import org.json.JSONObject;

public class Database {
   public static String connect () {
      JSONObject response = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         System.out.println("[SQLITE] Successfully connected to the database.");
         
         Statement stmt = connection.createStatement();
         
         stmt.execute(
            "CREATE TABLE IF NOT EXISTS storage (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, price REAL NOT NULL, length INTEGER NOT NULL, width INTEGER NOT NULL, quantity INTEGER NOT NULL, thickness INTEGER NOT NULL)"
         );

         stmt.execute(
            "CREATE TABLE IF NOT EXISTS materials (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, thickness INTEGER NOT NULL);"
         );

         stmt.execute(
            "CREATE TABLE IF NOT EXISTS clients (id INTEGER PRIMARY KEY AUTOINCREMENT,  name VARCHAR(255) NOT NULL, cpf VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, cel VARCHAR(255) NOT NULL);"
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
