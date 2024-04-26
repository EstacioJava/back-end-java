package com.estacio.demo.controller;

import java.sql.*;
import java.util.Locale;

public class Database {
   public static String connect () {
      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         System.out.println("[SQLITE] Successfully connected to the database.");
         Statement stmt = connection.createStatement();
         stmt.execute("CREATE TABLE IF NOT EXISTS MATERIALS (ID INTEGER PRIMARY KEY AUTOINCREMENT, MATERIAL_NAME VARCHAR(255), COST REAL)");
         return "[SQLITE] Successfully connected to the database.";
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String registerMaterial (String materialName, float cost) {
      // String responseStr = "";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         System.out.println(String.format(Locale.US, "[SQLITE] Inserting '%s' (MATERIAL_NAME) and %.2f (COST) into MATERIALS table...", materialName, cost));
         stmt.execute(String.format(Locale.US, "INSERT INTO MATERIALS (MATERIAL_NAME, COST) VALUES ('%s', %.2f)", materialName, cost));
         // ResultSet allMaterials = connection.prepareStatement("SELECT * FROM MATERIALS").executeQuery();
   
         // while (allMaterials.next()) {
         //    responseStr = String.format("%s\nID: %d / Name: %s", responseStr, allMaterials.getInt("ID"), allMaterials.getString("NAME"));
         //    // System.out.println("ID: " + allMaterials.getInt("ID") + " / NAME: " + allMaterials.getString("NAME"));
         // }
   
         return String.format(Locale.US, "[MATERIAL REGISTERED] MATERIAL_NAME: '%s' / COST: R$ %.2f", materialName, cost);
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String getMaterials () {
      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         String allMaterialsList = "[SQLITE] All Materials\n";
         ResultSet allMaterialsRS = connection.prepareStatement("SELECT * FROM MATERIALS").executeQuery();
   
         while (allMaterialsRS.next()) {
            allMaterialsList = String.format("%s\nMaterial: %s / Cost: R$ %s", allMaterialsList, allMaterialsRS.getInt("MATERIAL_NAME"), allMaterialsRS.getString("COST"));
            // System.out.println("ID: " + allMaterialsRS.getInt("ID") + " / NAME: " + allMaterialsRS.getString("NAME"));
         }
         return allMaterialsList;
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String clearMaterialsTable () {
      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         stmt.execute("DELETE FROM MATERIALS");
         return "[SQLITE] Table MATERIALS cleared.";
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }
}
