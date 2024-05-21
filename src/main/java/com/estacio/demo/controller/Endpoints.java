package com.estacio.demo.controller;

import org.json.JSONException;
import org.json.JSONObject;
import com.estacio.demo.model.Material;
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
        material.name = getJSONValue("name", request);
        material.thickness = Integer.parseInt(getJSONValue("thickness", request));

        return material.addMaterial();
    }

    @GetMapping("/materials")
    String getMaterials() {
        return Material.getMaterials();
    }

    @PostMapping("/storage")
    String addToStorage(@RequestBody String request) {
        Storage storageItem = new Storage();

        storageItem.name = getJSONValue("name", request);
        storageItem.length = Integer.parseInt(getJSONValue("length", request));
        storageItem.width = Integer.parseInt(getJSONValue("width", request));
        storageItem.price = Float.parseFloat(getJSONValue("price", request));
        storageItem.quantity = Integer.parseInt(getJSONValue("quantity", request));
        storageItem.thickness = Integer.parseInt(getJSONValue("thickness", request));

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
}