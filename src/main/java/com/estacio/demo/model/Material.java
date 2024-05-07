package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import org.json.JSONObject;

import com.estacio.demo.controller.Database;

public class Material {
   public String name = null;
   public Integer thickness = null;

   public String addMaterial () {
      JSONObject response = new JSONObject();
      Database.connect();

      try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
         response.put("name", name);
         response.put("thickness", thickness);
         
         Statement stmt = connection.createStatement();
         stmt.execute(String.format(Locale.US, "INSERT INTO materials (name, thickness) VALUES ('%s', %d)", name, thickness));
         System.out.println(String.format(Locale.US, "[SQLITE] %s :: %dmm", name, thickness));
   
         return response.toString();
      } catch (SQLException error) {
         response.put("error", error.getMessage());
         System.out.println(error.getMessage());
         return response.toString();
      }
   }
}