package com.estacio.demo.controller;

import java.sql.*;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

public class Database {
   public static String connect () {
      JSONObject response = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         System.out.println("[SQLITE] Successfully connected to the database.");
         
         Statement stmt = connection.createStatement();
         stmt.execute(
            "CREATE TABLE IF NOT EXISTS MATERIALS (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), price REAL, length INTEGER, width INTEGER)"
         );

         response.put("message", "Successfully connected to the database.");
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }

   public static String registerMaterial (String name, int length, int width, float price) {
      JSONObject response = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         response.put("name", name);
         response.put("length", length);
         response.put("width", width);
         response.put("price", price);
         
         Statement stmt = connection.createStatement();
         stmt.execute(String.format(Locale.US, "INSERT INTO MATERIALS (name, price, length, width) VALUES ('%s', %.2f, %d, %d)", name, price, length, width));
         System.out.println(String.format(Locale.US, "[SQLITE] %s :: %dfmm x %dfmm  -  R$ %f", name, length, width, price));
   
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }

   public static String getMaterials () {
      JSONArray materials = new JSONArray();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         ResultSet allMaterialsRS = connection.prepareStatement("SELECT * FROM MATERIALS").executeQuery();
   
         while (allMaterialsRS.next()) {
            JSONObject currentMaterial = new JSONObject();

            currentMaterial.put("id", allMaterialsRS.getString("id"));
            currentMaterial.put("name", allMaterialsRS.getString("name"));
            currentMaterial.put("length", allMaterialsRS.getInt("length"));
            currentMaterial.put("width", allMaterialsRS.getInt("width"));
            currentMaterial.put("price", allMaterialsRS.getFloat("price"));

            materials.put(currentMaterial);
         }
         return materials.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String getSingleMaterial (String materialID) {
      JSONObject material = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         ResultSet chosenMaterial = stmt.executeQuery(String.format("SELECT * FROM MATERIALS WHERE id = %s", materialID));
         System.out.println(chosenMaterial);
   
         while (chosenMaterial.next()) {
            material.put("id", chosenMaterial.getString("id"));
            material.put("name", chosenMaterial.getString("name"));
            material.put("length", chosenMaterial.getInt("length"));
            material.put("width", chosenMaterial.getInt("width"));
            material.put("price", chosenMaterial.getFloat("price"));
         }

         return material.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String clearMaterialsTable () {
      JSONObject response = new JSONObject();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         stmt.execute("DELETE FROM MATERIALS");
         response.put("message", "Table MATERIALS cleared.");
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }
}
