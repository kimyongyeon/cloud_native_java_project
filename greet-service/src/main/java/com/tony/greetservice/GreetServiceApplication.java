package com.tony.greetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class GreetServiceApplication {

   @GetMapping("/hi/{name}")
   public String greet(@PathVariable String name) {
      return "greet: " + name;
   }

   public static void main(String[] args) {
      SpringApplication.run(GreetServiceApplication.class, args);
   }

}
