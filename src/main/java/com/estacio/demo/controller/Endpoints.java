package com.estacio.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HelloController
 */
@RestController
public class Endpoints {
    @GetMapping("/register-material")
    public String registerMaterial() {
        Database.connect();
        return Database.registerMaterial("MDF :: 200mm x 160mm", 120.50f);
    }

    @GetMapping("/clear-materials")
    public String clearMaterialsTable() {
        Database.connect();
        return Database.clearMaterialsTable();
    }

    @GetMapping("/get-materials")
    public String getAllMaterials() {
        Database.connect();
        return Database.getMaterials();
    }
}