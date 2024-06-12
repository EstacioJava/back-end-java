package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

public class Order {

    private Integer clientID;
    private Storage storage;
    private String status;
    private String description;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private Float finalPrice;
    private Float laborCost;


    public String addOrder() {
        JSONObject response = new JSONObject();
        Database.connect(); 
    
        String querySQL = "INSERT INTO orders (clientID, status, description, orderDate, deliveryDate, finalPrice, laborCost) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

            pstmt.setInt(1, clientID);
            pstmt.setString(2, status);
            pstmt.setString(3, description);
            pstmt.setString(4, orderDate.toString());
            pstmt.setString(5, deliveryDate.toString());
            pstmt.setFloat(6, finalPrice);
            pstmt.setFloat(7, laborCost);


            pstmt.executeUpdate();

            response.put("clientID", clientID);
            response.put("status", status);
            response.put("description", description);
            response.put("orderDate", orderDate);
            response.put("deliveryDate", deliveryDate);
            response.put("finalPrice", finalPrice);
            response.put("laborCost", laborCost);
            
            System.out.println(String.format(Locale.US, 
                "[SQLITE::ADD ORDER] %d :: %s :: %s :: %s :: %s :: R$ %.2f :: R$ %.2f", clientID, status, description, orderDate, deliveryDate, finalPrice, laborCost));

            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String getOrders() {
        JSONArray allOrdersArray = new JSONArray();
        Database.connect();

        String querySQL = "SELECT * FROM orders";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet allOrders = pstmt.executeQuery()) {

            while (allOrders.next()) {
                JSONObject currentOrder = new JSONObject();
                currentOrder.put("id", allOrders.getInt("id"));
                currentOrder.put("clientID", allOrders.getInt("clientID"));
                currentOrder.put("status", allOrders.getString("status"));
                currentOrder.put("description", allOrders.getString("description"));
                currentOrder.put("orderDate", allOrders.getString("orderDate"));
                currentOrder.put("deliveryDate", allOrders.getString("deliveryDate"));
                currentOrder.put("finalPrice", allOrders.getFloat("finalPrice"));
                currentOrder.put("laborCost", allOrders.getFloat("laborCost"));

                allOrdersArray.put(currentOrder);
            }

            return allOrdersArray.toString();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException: " + error.getMessage();
        }
    }

    public static String getOrderById(Integer id) {
        JSONObject orderDetails = new JSONObject();
        Database.connect();

        String querySQL = "SELECT * FROM orders WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(querySQL)) {

            pstmt.setInt(1, id);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                orderDetails.put("id", resultSet.getInt("id"));
                orderDetails.put("clientID", resultSet.getInt("clientID"));
                orderDetails.put("status", resultSet.getString("status"));
                orderDetails.put("description", resultSet.getString("description"));
                orderDetails.put("orderDate", resultSet.getString("orderDate"));
                orderDetails.put("deliveryDate", resultSet.getString("deliveryDate"));
                orderDetails.put("finalPrice", resultSet.getFloat("finalPrice"));
                orderDetails.put("laborCost", resultSet.getFloat("laborCost"));

                return orderDetails.toString();
            } else {
                return "ID not found";
            }

        } catch (SQLException error) {
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException: " + error.getMessage();
        }
    }

    public String updateOrder(Integer id) {
        JSONObject response = new JSONObject();
        Database.connect();

        String checkSQL = "SELECT id FROM orders WHERE id = ?";
        String updateSQL = "UPDATE orders SET clientID = ?, status = ?, description = ?, orderDate = ?, deliveryDate = ?, finalPrice = ?, laborCost = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement checkStmt = connection.prepareStatement(checkSQL);     
             PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {

            checkStmt.setInt(1, id);
            ResultSet resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                response.put("error", "ID not found");
                System.out.println(String.format(Locale.US, "[SQLITE::CHECK ORDER] Order ID: %d not found", id));
                return response.toString();
            }
            updateStmt.setInt(1, clientID);
            updateStmt.setString(2, status);
            updateStmt.setString(3, description);
            updateStmt.setString(4, orderDate.toString());
            updateStmt.setString(5, deliveryDate.toString());
            updateStmt.setFloat(6, finalPrice);
            updateStmt.setFloat(7, laborCost);
            updateStmt.setInt(8, id);

            updateStmt.executeUpdate();

            response.put("id", id);
            response.put("clientID", clientID);
            response.put("status", status);
            response.put("description", description);
            response.put("orderDate", orderDate);
            response.put("deliveryDate", deliveryDate);
            response.put("finalPrice", finalPrice);
            response.put("laborCost", laborCost);

            System.out.println(String.format(Locale.US,"[SQLITE::UPDATE ORDER] %d :: %s :: %s :: %s :: %s :: R$ %.2f :: R$ %.2f", clientID, status, description, orderDate, deliveryDate, finalPrice, laborCost));

            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String deleteOrderById(Integer id) {
        JSONObject response = new JSONObject();
        Database.connect();

        String checkSQL = "SELECT id FROM orders WHERE id = ?";
        String deleteSQL = "DELETE FROM orders WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {

            checkStmt.setInt(1, id);
            ResultSet resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                response.put("error", "ID not found");
                System.out.println(String.format(Locale.US, "[SQLITE::CHECK ORDER] Order ID: %d não encontrado", id));
                return response.toString();
            }

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

            response.put("id", id);

            System.out.println(String.format(Locale.US, "[SQLITE::DELETE ORDER] Order ID: %d", id));

            return response.toString();
        } catch (SQLException error) {
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String deleteAllOrders() {
        JSONObject response = new JSONObject();
        Database.connect();

        String deleteSQL = "DELETE FROM orders";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db");
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

            int rowsAffected = pstmt.executeUpdate();

            response.put("rowsAffected", rowsAffected);
            response.put("message", "All orders have been successfully deleted.");

            System.out.println(String.format(Locale.US, 
                "[SQLITE::DELETE ALL ORDERS] Rows Deleted: %d",rowsAffected));

            return response.toString();
        } catch (SQLException error) {
            System.out.println(error.getMessage());
            response.put("error", error.getMessage());
            return response.toString();
        }
    }
}
