package com.estacio.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.estacio.demo.model.Client;
import com.estacio.demo.model.Material;
import com.estacio.demo.model.Order;
import com.estacio.demo.model.Storage;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



/**
 * HelloController
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Endpoints {
    String getJSONValue (String key, String jsonObject) {
        JSONObject object = new JSONObject(jsonObject);

        try {
            return object.get(key).toString();
        } catch (JSONException jsonException) {
            String errorMessage = String.format("[JSONException] The property \"%s\" is missing.", key);
            System.out.println(errorMessage);
            System.out.println("\t\t - " + jsonException);
            return errorMessage;
        }
    }

    @PostMapping("/materials")
    String registerMaterial(@RequestBody String request) {
        Material material = new Material();
        material.setName(getJSONValue("name", request));
        material.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return material.addMaterial();
    }

    @GetMapping("/materials")
    String getMaterials() {
        return Material.getMaterials();
    }

    @PostMapping("/storage")
    String addToStorage(@RequestBody String request) {
        Storage storageItem = new Storage();

        storageItem.setName(getJSONValue("name", request));
        storageItem.setLength(Integer.parseInt(getJSONValue("length", request)));
        storageItem.setWidth(Integer.parseInt(getJSONValue("width", request)));
        storageItem.setPrice(Float.parseFloat(getJSONValue("price", request)));
        storageItem.setQuantity(Integer.parseInt(getJSONValue("quantity", request)));
        storageItem.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return storageItem.addItemToStorage();
    }

    @GetMapping("/storage")
    String getStorageItems() {
        return Storage.getStorageItems();
    }
    
    @GetMapping("/storage/{id}")
    String getSingleStorageItem(@PathVariable("id") String id) {
        return Storage.getSingleStorageItem(id);
    }

    @DeleteMapping("/storage") 
    String clearStorage() {
        return Storage.clearStorage();
    }
    
    @DeleteMapping("/storage/{id}") 
    String deleteStorageItemByID(@PathVariable String id) {
        return Storage.deleteStorageItemByID(id);
    }

    @GetMapping("/clients")
    String getClients(){
        return Client.getClients();
    }

    @PostMapping("/clients")
    String addClient(@RequestBody String request){
        Client client = new Client();

        client.setName(getJSONValue("name", request));
        client.setCpf(getJSONValue("cpf", request));
        client.setCel(getJSONValue("cel", request));
        client.setEmail(getJSONValue("email", request));

        return client.addClient();
    }

    @PutMapping("/clients/{id}")
    public String updateClient(@PathVariable Integer id, @RequestBody String request) {
        Client client = new Client();

        client.setName(getJSONValue("name", request));
        client.setCpf(getJSONValue("cpf", request));
        client.setCel(getJSONValue("cel", request));
        client.setEmail(getJSONValue("email", request));

        return client.updateClient(id);
    }
    String updateClient(@RequestBody String request){
        Order order = new Order();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dateFormatter = dateFormatter.withLocale( Locale.US );
        String strOrderDate = getJSONValue("orderDate", request);
        String strDeliveryDate = getJSONValue("deliveryDate", request);

        order.setClientID(Integer.parseInt(getJSONValue("clientID", request)));
        order.setStatus(getJSONValue("status", request));
        order.setDescription(getJSONValue("description", request));
        order.setOrderDate(LocalDate.parse(strOrderDate, dateFormatter));
        order.setDeliveryDate(LocalDate.parse(strDeliveryDate, dateFormatter));
        order.setFinalPrice(Float.parseFloat(getJSONValue("finalPrice", request)));

        return order.addOrder();
    }

    @GetMapping("/orders")
    String getOrders(){
        return Order.getOrders();
    }

    @PostMapping("/orders")
    String addOrder(@RequestBody String request){
        Order order = new Order();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dateFormatter = dateFormatter.withLocale( Locale.US );
        String strOrderDate = getJSONValue("orderDate", request);
        String strDeliveryDate = getJSONValue("deliveryDate", request);

        order.setClientID(Integer.parseInt(getJSONValue("clientID", request)));
        order.setStatus(getJSONValue("status", request));
        order.setDescription(getJSONValue("description", request));
        order.setOrderDate(LocalDate.parse(strOrderDate, dateFormatter));
        order.setDeliveryDate(LocalDate.parse(strDeliveryDate, dateFormatter));
        order.setFinalPrice(Float.parseFloat(getJSONValue("finalPrice", request)));

        return order.addOrder();
    }
}
