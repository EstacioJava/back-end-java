package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.estacio.demo.controller.Database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Material {
   
   private String name = null;
   private Integer thickness = null;

   public String addMaterial() {
      JSONObject response = new JSONObject();
      Database.connect();

      String insertSQL = "INSERT INTO materials (name, thickness) VALUES (?, ?)";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

          pstmt.setString(1, name);
          pstmt.setInt(2, thickness);

          pstmt.executeUpdate();

          response.put("name", name);
          response.put("thickness", thickness);

          System.out.println(String.format(Locale.US, 
              "[SQLITE::ADD MATERIAL] %s :: %d", name, thickness));

          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
   }

   public static String getMaterials() {
      JSONArray allMaterialsArray = new JSONArray();
      Database.connect();

      String querySQL = "SELECT * FROM materials";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(querySQL);
           ResultSet allMaterials = pstmt.executeQuery()) {

          while (allMaterials.next()) {
              JSONObject currentMaterial = new JSONObject();
              currentMaterial.put("id", allMaterials.getInt("id"));
              currentMaterial.put("name", allMaterials.getString("name"));
              currentMaterial.put("thickness", allMaterials.getInt("thickness"));

              allMaterialsArray.put(currentMaterial);
          }

          return allMaterialsArray.toString();
      } catch (SQLException error) {
          System.out.println(error.getMessage());
          return "[SQLITE - ERROR] SQLException: " + error.getMessage();
      }
   }

   public static String getMaterialById(Integer id) {
      JSONObject materialDetails = new JSONObject();
      Database.connect();

      String querySQL = "SELECT * FROM materials WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

          pstmt.setInt(1, id);

          ResultSet resultSet = pstmt.executeQuery();

          if (resultSet.next()) {
              materialDetails.put("id", resultSet.getInt("id"));
              materialDetails.put("name", resultSet.getString("name"));
              materialDetails.put("thickness", resultSet.getInt("thickness"));

              return materialDetails.toString();
          } else {
              return "ID not found";
          }

      } catch (SQLException error) {
          System.out.println(error.getMessage());
          return "[SQLITE - ERROR] SQLException: " + error.getMessage();
      }
   }

   public String updateMaterial(Integer id) {
      JSONObject response = new JSONObject();
      Database.connect();

      String checkSQL = "SELECT id FROM materials WHERE id = ?";
      String updateSQL = "UPDATE materials SET name = ?, thickness = ? WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
           PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {

          checkStmt.setInt(1, id);
          ResultSet resultSet = checkStmt.executeQuery();

          if (!resultSet.next()) {
              response.put("error", "ID not found");
              System.out.println(String.format(Locale.US, "[SQLITE::CHECK MATERIAL] Material ID: %d not found", id));
              return response.toString();
          }

          updateStmt.setString(1, name);
          updateStmt.setInt(2, thickness);
          updateStmt.setInt(3, id);

          updateStmt.executeUpdate();

          response.put("id", id);
          response.put("name", name);
          response.put("thickness", thickness);

          System.out.println(String.format(Locale.US, "[SQLITE::UPDATE MATERIAL] %d :: %s :: %d", id, name, thickness));

          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
   }

   public static String deleteMaterialById(Integer id) {
      JSONObject response = new JSONObject();
      Database.connect();

      String checkSQL = "SELECT id FROM materials WHERE id = ?";
      String deleteSQL = "DELETE FROM materials WHERE id = ?";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
           PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {

          checkStmt.setInt(1, id);
          ResultSet resultSet = checkStmt.executeQuery();

          if (!resultSet.next()) {
              response.put("error", "ID not found");
              System.out.println(String.format(Locale.US, "[SQLITE::CHECK MATERIAL] Material ID: %d not found", id));
              return response.toString();
          }

          deleteStmt.setInt(1, id);
          deleteStmt.executeUpdate();

          response.put("id", id);

          System.out.println(String.format(Locale.US, "[SQLITE::DELETE MATERIAL] Material ID: %d", id));

          return response.toString();
      } catch (SQLException error) {
          response.put("error", error.getMessage());
          System.out.println(error.getMessage());
          return response.toString();
      }
   }

   public static String deleteAllMaterials() {
      JSONObject response = new JSONObject();
      Database.connect();

      String deleteSQL = "DELETE FROM materials";

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
           PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

          int rowsAffected = pstmt.executeUpdate();

          response.put("rowsAffected", rowsAffected);
          response.put("message", "All materials have been successfully deleted.");

          System.out.println(String.format(Locale.US, 
              "[SQLITE::DELETE ALL MATERIALS] Rows Deleted: %d", rowsAffected));

          return response.toString();
      } catch (SQLException error) {
          System.out.println(error.getMessage());
          response.put("error", error.getMessage());
          return response.toString();
      }
   }
}