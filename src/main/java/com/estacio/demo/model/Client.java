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

public class Client{
    private String name;
    private String cpf;
    private String email;
    private String cel;

    public String addClient() {
        JSONObject response = new JSONObject();
        Database.connect();

        String insertSQL = "INSERT INTO clients (name, cpf, email, cel) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            pstmt.setString(1, name);
            pstmt.setString(2, cpf);
            pstmt.setString(3, email);
            pstmt.setString(4, cel);

            pstmt.executeUpdate();

            response.put("name", name);
            response.put("cpf", cpf);
            response.put("email", email);
            response.put("cel", cel);

            System.out.println(String.format(Locale.US, "[SQLITE] %s :: %s :: %s :: %s", name, cpf, email, cel));
        
            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String getClients() {
        JSONArray allClientsArray = new JSONArray();
        Database.connect();

        String querySQL = "SELECT * FROM clients";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet allClients = pstmt.executeQuery()) {

            while (allClients.next()) {
                JSONObject currentClient = new JSONObject();
                currentClient.put("id", allClients.getString("id"));
                currentClient.put("name", allClients.getString("name"));
                currentClient.put("cpf", allClients.getString("cpf"));
                currentClient.put("email", allClients.getString("email"));
                currentClient.put("cel", allClients.getString("cel"));

                allClientsArray.put(currentClient);
            }

            return allClientsArray.toString();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException: " + error.getMessage();
        }
    }

    public static String getClientById(Integer id) {
        JSONObject clientDetails = new JSONObject();
        Database.connect();

        String querySQL = "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                clientDetails.put("id", resultSet.getInt("id"));
                clientDetails.put("name", resultSet.getString("name"));
                clientDetails.put("cpf", resultSet.getString("cpf"));
                clientDetails.put("email", resultSet.getString("email"));
                clientDetails.put("cel", resultSet.getString("cel"));

                return clientDetails.toString();
            } else {
                return "ID not found";
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException: " + error.getMessage();
        }
    }


    public String updateClient(Integer id) {
        JSONObject response = new JSONObject();
        Database.connect();

        String checkSQL = "SELECT id FROM clients WHERE id = ?";
        String updateSQL = "UPDATE clients SET name = ?, cpf = ?, email = ?, cel = ? WHERE id = ?";
        

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
             PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {

            checkStmt.setInt(1, id);
            ResultSet resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                response.put("error", "ID not found");
                System.out.println(String.format(Locale.US, "[SQLITE::CHECK CLIENT] Client ID: %d not found", id));
                return response.toString();
            }
            updateStmt.setString(1, name);
            updateStmt.setString(2, cpf);
            updateStmt.setString(3, email);
            updateStmt.setString(4, cel);
            updateStmt.setInt(5, id);
        
            updateStmt.executeUpdate();

            response.put("id", id);
            response.put("name", name);
            response.put("cpf", cpf);
            response.put("email", email);
            response.put("cel", cel);

            System.out.println(String.format(Locale.US, "[SQLITE::UPDATE CLIENT] %s :: %s :: %s :: %s", name, cpf, email, cel));
            
            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }

        
    }

    public static String deleteClientById(Integer id) {
        JSONObject response = new JSONObject();
        Database.connect();

        String checkSQL = "SELECT id FROM clients WHERE id = ?";
        String deleteSQL = "DELETE FROM clients WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {

            checkStmt.setInt(1, id);
            ResultSet resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                response.put("error", "ID not found");
                System.out.println(String.format(Locale.US, "[SQLITE::CHECK CLIENT] Client ID: %d não encontrado", id));
                return response.toString();
            }

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

            response.put("id", id);

            System.out.println(String.format(Locale.US, "[SQLITE::DELETE CLIENT] Client ID: %d", id));

            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String deleteAllClients() {
        JSONObject response = new JSONObject();
        Database.connect();

        String deleteSQL = "DELETE FROM clients";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

            int rowsAffected = pstmt.executeUpdate();

            response.put("rowsAffected", rowsAffected);
            response.put("message", "All clients have been successfully deleted.");

            System.out.println(String.format(Locale.US, "[SQLITE::DELETE ALL CLIENTS] Rows Deleted: %d", rowsAffected));

            return response.toString();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
            response.put("error", error.getMessage());
            return response.toString();
        }
    }



}
