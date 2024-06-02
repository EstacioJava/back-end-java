package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import com.estacio.demo.controller.Database;


public class Order {

    @Id
    public int id;

    public Client client;
    public Storage storage;
    public String status;
    public String description;
    public LocalDate orderDate;
    public LocalDate deliveryDate;
    public float finalPrice;

    public String addOrder(){
        JSONObject response = new JSONObject();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            response.put("status", status);
            response.put("description", description);
            response.put("orderDate", orderDate);
            response.put("deliveryDate", deliveryDate);
            response.put("finalPrice", finalPrice);

            Statement stmt = connection.createStatement();
            stmt.execute(String.format(Locale.US, "INSERT INTO orders (status, description, orderDate, deliveryDate, finalPrice) VALUES ('%s', '%s', '%s', '%s', %.2f)", description, orderDate, deliveryDate, finalPrice));
            System.out.println(String.format(Locale.US, "[SQLITE] %s :: %s :: %s :: %s:: R$ %f", status, description, orderDate, deliveryDate, finalPrice));
        
            return response.toString();
        }catch(SQLException error){
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public String getOrders(){
        JSONArray allOrdersArray = new JSONArray();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            ResultSet allOrders = connection.prepareStatement("SELECT * FROM orders").executeQuery();
            
            while(allOrders.next()){
                JSONObject currentOrder = new JSONObject();
                currentOrder.put("status", allOrders.getString("status"));
                currentOrder.put("description", allOrders.getString("description"));
                currentOrder.put("orderDate", allOrders.getString("orderDate"));
                currentOrder.put("deliveryDate", allOrders.getString("deliveryDate"));
                currentOrder.put("finalPrice", allOrders.getFloat("finalPrice"));

                allOrdersArray.put(currentOrder);
            }

            return allOrdersArray.toString();
        }catch (SQLException error){
            System.out.println(error.getMessage());
            return "[SQLITE - ERROR] SQLException";
        }
    }

}
