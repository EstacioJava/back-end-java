package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.estacio.demo.controller.Database;

public class Client{
    public String name;
    public String cpf;
    public String email;
    public String cel;

    public String addClient(){
        JSONObject response = new JSONObject();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            response.put("name", name);
            response.put("cpf", cpf);
            response.put("email", email);
            response.put("cel", cel);

            Statement stmt = connection.createStatement();
            stmt.execute(String.format(Locale.US, "INSERT INTO clients (name, cpf, email, cel) VALUES ('%s', '%s', '%s', '%s')", name, cpf, email, cel));
            System.out.println(String.format(Locale.US, "[SQLITE] %s :: %s :: %s :: %s", name, cpf, email, cel));
        
            return response.toString();
        }catch(SQLException error){
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }

    }
    public static String getClients(){
        JSONArray allClientsArray = new JSONArray();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            ResultSet allClients = connection.prepareStatement("SELECT * FROM clients").executeQuery();
            
            while(allClients.next()){
                JSONObject currentClient = new JSONObject();
                currentClient.put("id", allClients.getString("id"));
                currentClient.put("name", allClients.getString("name"));
                currentClient.put("cpf", allClients.getInt("cpf"));
                currentClient.put("email", allClients.getString("email"));
                currentClient.put("cel", allClients.getInt("cel"));

                allClientsArray.put(currentClient);
            }

            return allClientsArray.toString();
        }catch (SQLException error){
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException";
        }
    }
}