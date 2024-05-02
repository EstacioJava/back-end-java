package com.estacio.demo.controller;

import org.json.JSONException;
import org.json.JSONObject;
import com.estacio.demo.model.Material;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * HelloController
 */
@RestController
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
        material.setLength(Integer.parseInt(getJSONValue("length", request)));
        material.setWidth(Integer.parseInt(getJSONValue("width", request)));
        material.setPrice(Float.parseFloat(getJSONValue("price", request)));

        if (
            material.name != null &&
            material.length != null &&
            material.width != null &&
            material.price != null
        ) {
            return Database.registerMaterial(material.name, material.length, material.width, material.price);
        } else {
            return "[ERROR] There are propeties missing.";
        } 
    }

    @GetMapping("/materials")
    String getAllMaterials() {
        return Database.getMaterials();
    }
    
    @GetMapping("/materials/{id}")
    String getOneMaterial(@PathVariable("id") String id) {
        return Database.getSingleMaterial(id);
    }

    @DeleteMapping("/materials") 
    String clearMaterialsTable() {
        return Database.clearMaterialsTable();
    }
}