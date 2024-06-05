package com.estacio.demo.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Locale;
import com.estacio.demo.controller.Database;

import org.json.JSONArray;
import org.json.JSONObject;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Storage {
   private String name = null;
   private Integer thickness = null;
   private Integer length = null;
   private Integer width = null;
   private Integer quantity = null;
   private Float price = null;

   public String addItemToStorage () {
      JSONObject response = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         response.put("name", name);
         response.put("length", length);
         response.put("width", width);
         response.put("price", price);
         response.put("quantity", quantity);
         response.put("thickness", thickness);
         
         Statement stmt = connection.createStatement();
         if (name != null && length != null && width != null && price != null && quantity != null && thickness != null) {
            stmt.execute(String.format(Locale.US, "INSERT INTO storage (name, price, length, width, quantity, thickness) VALUES ('%s', %.2f, %d, %d, %d, %d)", name, price, length, width, quantity, thickness));
            System.out.println(String.format(Locale.US, "[SQLITE] %s :: %dmm x %dmm x %dmm -  R$ %f (%dx)", name, length, width, thickness, price, quantity));
         } else {
            System.out.println(String.format(Locale.US, "[SQLITE] %s :: %dmm x %dmm x %dmm -  R$ %f (%dx)", name, length, width, thickness, price, quantity));
            return response.put("error", "[SQLITE::ERROR] There are propeties missing.").toString();
         } 
   
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }

   public static String getStorageItems () {
      JSONArray materials = new JSONArray();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         ResultSet allItems = connection.prepareStatement("SELECT * FROM storage").executeQuery();
   
         while (allItems.next()) {
            JSONObject currentMaterial = new JSONObject();

            currentMaterial.put("id", allItems.getString("id"));
            currentMaterial.put("name", allItems.getString("name"));
            currentMaterial.put("length", allItems.getInt("length"));
            currentMaterial.put("width", allItems.getInt("width"));
            currentMaterial.put("price", allItems.getFloat("price"));
            currentMaterial.put("quantity", allItems.getInt("quantity"));
            currentMaterial.put("thickness", allItems.getInt("thickness"));

            materials.put(currentMaterial);
         }
         
         return materials.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String getSingleStorageItem (String materialID) {
      JSONObject material = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         ResultSet chosenMaterial = stmt.executeQuery(String.format("SELECT * FROM storage WHERE id = %s", materialID));
         System.out.println(chosenMaterial);
   
         while (chosenMaterial.next()) {
            material.put("id", chosenMaterial.getString("id"));
            material.put("name", chosenMaterial.getString("name"));
            material.put("length", chosenMaterial.getInt("length"));
            material.put("width", chosenMaterial.getInt("width"));
            material.put("price", chosenMaterial.getFloat("price"));
            material.put("thickness", chosenMaterial.getInt("thickness"));
            material.put("quantity", chosenMaterial.getInt("quantity"));
         }

         return material.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }

   public static String clearStorage () {
      JSONObject response = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         stmt.execute("DELETE FROM storage");
         response.put("message", "Table storage cleared.");
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }
   
   public static String deleteStorageItemByID (String id) {
      JSONObject response = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         Statement stmt = connection.createStatement();
         ResultSet itemToDelete = stmt.executeQuery(String.format("SELECT name FROM storage WHERE id = %s", id));
         
         if (itemToDelete.getString("name") != null) {
            stmt.execute(String.format("DELETE FROM storage WHERE id = %s", id));
            response.put("message", String.format("Item with ID %s deleted from the database.", id));
         } else {
            response.put("error", String.format("No item with ID %s found.", id));
         }

         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }

   public String updateStorageItem(Integer id) {
        JSONObject response = new JSONObject();
        Database.connect();

        String updateSQL = "UPDATE storage SET name = ?, thickness = ?, length = ?, width = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)){

            pstmt.setString(1, name);
            pstmt.setInt(2, thickness);
            pstmt.setInt(3, length);
            pstmt.setInt(4, width);
            pstmt.setInt(5, quantity);
            pstmt.setFloat(6, price);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();

            response.put("id", id);
            response.put("name", name);
            response.put("thickness", thickness);
            response.put("length", length);
            response.put("width", width);
            response.put("quantity", quantity);
            response.put("price", price);

            System.out.println(String.format(Locale.US,"[SQLITE::UPDATE STORAGE] %s :: %d :: %d :: %d :: %d :: %f", name, thickness, length, width, quantity, price));

            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }
}