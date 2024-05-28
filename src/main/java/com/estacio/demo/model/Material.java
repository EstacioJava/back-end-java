package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.estacio.demo.controller.Database;

public class Material {
   public String name = null;
   public Integer thickness = null;

   public String addMaterial () {
      JSONObject response = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         PreparedStatement pStmt = connection.prepareStatement("INSERT INTO materials (name, thickness) VALUES (?, ?)");
         pStmt.setString(1, name);
         pStmt.setInt(2, thickness);
         pStmt.executeUpdate();

         System.out.println(String.format(Locale.US, "[SQLITE] %s :: %dmm", name, thickness));

         connection.close();
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }

   public static String getMaterials () {
      JSONArray allMaterialsArray = new JSONArray();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         ResultSet allMaterials = connection.prepareStatement("SELECT * FROM materials").executeQuery();

         while (allMaterials.next()) {
            JSONObject currentMaterial = new JSONObject();

            currentMaterial.put("name", allMaterials.getString("name"));
            currentMaterial.put("thickness", allMaterials.getInt("thickness"));

            allMaterialsArray.put(currentMaterial);
         }

         return allMaterialsArray.toString();
      } catch (SQLException error) {
         System.out.println(error.getMessage());
         return "[SQLITE - ERROR] SQLException";
      }
   }
}