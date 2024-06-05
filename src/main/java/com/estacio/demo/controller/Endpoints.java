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






    //materials

    @GetMapping("/materials")
    String getMaterials() {
        return Material.getMaterials();
    }

    @GetMapping("/materials/{id}")
    String getMaterialById(@PathVariable("id") Integer id) {
        return Material.getMaterialById(id);
    }

    @PostMapping("/materials")
    String addMaterial(@RequestBody String request) {
        Material material = new Material();
        material.setName(getJSONValue("name", request));
        material.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return material.addMaterial();
    }

    @PutMapping("/materials/{id}")
    String updateMaterial(@PathVariable("id") Integer id, @RequestBody String request) {
        Material material = new Material();
        material.setName(getJSONValue("name", request));
        material.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return material.updateMaterial(id);
    }

    @DeleteMapping("/materials") 
    String deleteMaterial() {
        return Material.deleteAllMaterials();
    }
    
    @DeleteMapping("/materials/{id}") 
    String deleteMaterialByI(@PathVariable Integer id) {
        return Material.deleteMaterialById(id);
    }

    




    //storage

    @GetMapping("/storage")
    String getStorageItems() {
        return Storage.getStorageItems();
    }
    
    @GetMapping("/storage/{id}")
    String getStorageItemById(@PathVariable("id") Integer id) {
        return Storage.getStorageItemById(id);
    }

    @PostMapping("/storage")
    String addItemToStorage(@RequestBody String request) {
        Storage storageItem = new Storage();

        storageItem.setName(getJSONValue("name", request));
        storageItem.setLength(Integer.parseInt(getJSONValue("length", request)));
        storageItem.setWidth(Integer.parseInt(getJSONValue("width", request)));
        storageItem.setPrice(Float.parseFloat(getJSONValue("price", request)));
        storageItem.setQuantity(Integer.parseInt(getJSONValue("quantity", request)));
        storageItem.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return storageItem.addItemToStorage();
    }

    @PutMapping("/storage/{id}")
    String updateStorageItem(@PathVariable("id") Integer id, @RequestBody String request) {
        Storage storageItem = new Storage();

        storageItem.setName(getJSONValue("name", request));
        storageItem.setLength(Integer.parseInt(getJSONValue("length", request)));
        storageItem.setWidth(Integer.parseInt(getJSONValue("width", request)));
        storageItem.setPrice(Float.parseFloat(getJSONValue("price", request)));
        storageItem.setQuantity(Integer.parseInt(getJSONValue("quantity", request)));
        storageItem.setThickness(Integer.parseInt(getJSONValue("thickness", request)));

        return storageItem.updateStorageItem(id);
    }

    @DeleteMapping("/storage") 
    String deleteStorages() {
        return Storage.deleteAllStorageItems();
    }
    
    @DeleteMapping("/storage/{id}") 
    String deleteStorageItemByID(@PathVariable Integer id) {
        return Storage.deleteStorageItemById(id);
    }








    //clients
    @GetMapping("/clients")
    String getClients(){
        return Client.getClients();
    }

    @GetMapping("/clients/{id}")
    String getClientsById(@PathVariable Integer id){
        return Client.getClientById(id);
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

    @DeleteMapping("/clients/{id}") 
    String deleteClientById(@PathVariable Integer id) {
        return Client.deleteClientById(id);
    }

    @DeleteMapping("/clients") 
    String deleteClients() {
        return Client.deleteAllClients();
    }

    




    //orders
    @GetMapping("/orders")
    String getOrders(){
        return Order.getOrders();
    }

    @GetMapping("/orders/{id}")
    String getOrdersById(@PathVariable Integer id){
        return Order.getOrderById(id);
    }

    @PostMapping("/orders")
    String addOrder(@RequestBody String request){
        Order order = new Order();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

    @PutMapping("/orders/{id}")
    public String updateOrder(@PathVariable Integer id, @RequestBody String request){
        Order order = new Order();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateFormatter = dateFormatter.withLocale( Locale.US );
        String strOrderDate = getJSONValue("orderDate", request);
        String strDeliveryDate = getJSONValue("deliveryDate", request);

        order.setClientID(Integer.parseInt(getJSONValue("clientID", request)));
        order.setStatus(getJSONValue("status", request));
        order.setDescription(getJSONValue("description", request));
        order.setOrderDate(LocalDate.parse(strOrderDate, dateFormatter));
        order.setDeliveryDate(LocalDate.parse(strDeliveryDate, dateFormatter));
        order.setFinalPrice(Float.parseFloat(getJSONValue("finalPrice", request)));

        return order.updateOrder(id);
    }

    @DeleteMapping("/orders/{id}") 
    String deleteOrderById(@PathVariable Integer id) {
        return Order.deleteOrderById(id);
    }

    @DeleteMapping("/orders") 
    String deleteOrders() {
        return Order.deleteAllOrders();
    }
}
