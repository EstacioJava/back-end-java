package com.estacio.demo.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
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

   public String addItemToStorage() {
      JSONObject response = new JSONObject();
      Database.connect();

      String querySQL = "INSERT INTO storage (name, thickness, length, width, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

          pstmt.setString(1, name);
          pstmt.setInt(2, thickness);
          pstmt.setInt(3, length);
          pstmt.setInt(4, width);
          pstmt.setInt(5, quantity);
          pstmt.setFloat(6, price);

          pstmt.executeUpdate();

          response.put("name", name);
          response.put("thickness", thickness);
          response.put("length", length);
          response.put("width", width);
          response.put("quantity", quantity);
          response.put("price", price);

          System.out.println(String.format(Locale.US, "[SQLITE] %s :: %d :: %d :: %d :: %d :: %.2f", name, thickness, length, width, quantity, price));
      
          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
  }


   public static String getStorageItems() {
      JSONArray allStorageItemsArray = new JSONArray();
      Database.connect();

      String querySQL = "SELECT * FROM storage";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
         PreparedStatement pstmt = connection.prepareStatement(querySQL);
         ResultSet allStorageItems = pstmt.executeQuery()) {

         while (allStorageItems.next()) {
            JSONObject currentStorageItem = new JSONObject();
            currentStorageItem.put("id", allStorageItems.getInt("id"));
            currentStorageItem.put("name", allStorageItems.getString("name"));
            currentStorageItem.put("thickness", allStorageItems.getInt("thickness"));
            currentStorageItem.put("length", allStorageItems.getInt("length"));
            currentStorageItem.put("width", allStorageItems.getInt("width"));
            currentStorageItem.put("quantity", allStorageItems.getInt("quantity"));
            currentStorageItem.put("price", allStorageItems.getFloat("price"));

            allStorageItemsArray.put(currentStorageItem);
         }

         return allStorageItemsArray.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException: " + error.getMessage();
      }
   }

   public static String getStorageItemById(Integer id) {
      JSONObject storageItemsDetail = new JSONObject();
      Database.connect();

      String querySQL = "SELECT * FROM storage WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

          pstmt.setInt(1, id);

          ResultSet resultSet = pstmt.executeQuery();

          if (resultSet.next()) {
              storageItemsDetail.put("id", resultSet.getInt("id"));
              storageItemsDetail.put("name", resultSet.getString("name"));
              storageItemsDetail.put("thickness", resultSet.getInt("thickness"));
              storageItemsDetail.put("length", resultSet.getInt("length"));
              storageItemsDetail.put("width", resultSet.getInt("width"));
              storageItemsDetail.put("quantity", resultSet.getInt("quantity"));
              storageItemsDetail.put("price", resultSet.getFloat("price"));

              return storageItemsDetail.toString();
          } else {
              return "ID not found";
          }

      } catch (SQLException error) {
          System.out.println(error.getMessage());
          return "[SQLITE - ERROR] SQLException: " + error.getMessage();
      }
   }

   public String updateStorageItem(Integer id) {
      JSONObject response = new JSONObject();
      Database.connect();

      String checkSQL = "SELECT id FROM storage WHERE id = ?";
      String updateSQL = "UPDATE storage SET name = ?, thickness = ?, length = ?, width = ?, quantity = ?, price = ? WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
           PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {

          checkStmt.setInt(1, id);
          ResultSet resultSet = checkStmt.executeQuery();

          if (!resultSet.next()) {
              response.put("error", "ID not found");
              System.out.println(String.format(Locale.US, "[SQLITE::CHECK STORAGE ITEM] Storage Item ID: %d not found", id));
              return response.toString();
          }

          updateStmt.setString(1, name);
          updateStmt.setInt(2, thickness);
          updateStmt.setInt(3, length);
          updateStmt.setInt(4, width);
          updateStmt.setInt(5, quantity);
          updateStmt.setFloat(6, price);
          updateStmt.setInt(7, id);

          updateStmt.executeUpdate();

          response.put("id", id);
          response.put("name", name);
          response.put("thickness", thickness);
          response.put("length", length);
          response.put("width", width);
          response.put("quantity", quantity);
          response.put("price", price);

          System.out.println(String.format(Locale.US, "[SQLITE::UPDATE STORAGE ITEM] %d :: %s :: %d :: %d :: %d :: %d :: %.2f", id, name, thickness, length, width, quantity, price));

          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
   }

   public static String deleteStorageItemById(Integer id) {
      JSONObject response = new JSONObject();
      Database.connect();

      String checkSQL = "SELECT id FROM storage WHERE id = ?";
      String deleteSQL = "DELETE FROM storage WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
           PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {

          checkStmt.setInt(1, id);
          ResultSet resultSet = checkStmt.executeQuery();

          if (!resultSet.next()) {
              response.put("error", "ID not found");
              System.out.println(String.format(Locale.US, "[SQLITE::CHECK STORAGE ITEM] Storage ID: %d not found", id));
              return response.toString();
          }

          deleteStmt.setInt(1, id);
          deleteStmt.executeUpdate();

          response.put("id", id);

          System.out.println(String.format(Locale.US, "[SQLITE::DELETE STORAGE ITEM] Storage ID: %d", id));

          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
   }

   public static String deleteAllStorageItems() {
      JSONObject response = new JSONObject();
      Database.connect();

      String deleteSQL = "DELETE FROM storage";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

          int rowsAffected = pstmt.executeUpdate();

          response.put("rowsAffected", rowsAffected);
          response.put("message", "All storage items have been successfully deleted.");

          System.out.println(String.format(Locale.US, 
              "[SQLITE::DELETE ALL STORAGES] Rows Deleted: %d", rowsAffected));

          return response.toString();
      } catch (SQLException error) {
          System.out.println(error.getMessage());
          response.put("error", error.getMessage());
          return response.toString();
      }
   }

   public void calculateFinalPrice(){
    
   }
}
   
  
   