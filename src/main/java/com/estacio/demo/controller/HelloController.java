package com.estacio.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HelloController
 */
@RestController
public class HelloController {

   @GetMapping("/users")
   public String getUsers() {
       return new String("You got users!");
   }

   @GetMapping("/accounts")
   public String getAccounts() {
       return new String("You got accounts!");
   }
   
}