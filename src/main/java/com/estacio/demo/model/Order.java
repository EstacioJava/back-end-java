package com.estacio.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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

    public String addOrder(){
        JSONObject response = new JSONObject();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            response.put("clientID", clientID);
            response.put("status", status);
            response.put("description", description);
            response.put("orderDate", orderDate);
            response.put("deliveryDate", deliveryDate);
            response.put("finalPrice", finalPrice);

            Statement stmt = connection.createStatement();
            System.out.println(String.format(Locale.US, "[SQLITE] %d :: %s :: %s :: %s :: %s:: R$ %.2f", clientID, status, description, orderDate, deliveryDate, finalPrice));
            stmt.execute(String.format(Locale.US, "INSERT INTO orders (clientID, status, description, orderDate, deliveryDate, finalPrice) VALUES (%d, '%s', '%s', '%s', '%s', %.2f)", clientID, status, description, orderDate, deliveryDate, finalPrice));
        
            return response.toString();
        }catch(SQLException error){
            response.put("error", error.getMessage());
            System.out.println(error.getMessage());
            return response.toString();
        }
    }

    public static String getOrders(){
        JSONArray allOrdersArray = new JSONArray();
        Database.connect();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/sqlite/db/data.db")) {
            ResultSet allOrders = connection.prepareStatement("SELECT * FROM orders").executeQuery();
            
            while(allOrders.next()){
                JSONObject currentOrder = new JSONObject();
                currentOrder.put("id", allOrders.getInt("id"));
                currentOrder.put("clientID", allOrders.getInt("clientID"));
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
