package com.tony.msaarchitecturecoffeeorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class MsaArchitectureCoffeeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaArchitectureCoffeeOrderApplication.class, args);
    }

}
