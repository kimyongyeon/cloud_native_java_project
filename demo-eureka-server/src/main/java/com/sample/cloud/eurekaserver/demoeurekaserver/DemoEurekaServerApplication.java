package com.sample.cloud.eurekaserver.demoeurekaserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
@Slf4j
public class DemoEurekaServerApplication implements CommandLineRunner {

   @Value("${spring.profiles:dev}")
   private String devProfile;

   public static void main(String[] args) {
      SpringApplication.run(DemoEurekaServerApplication.class, args);
   }

   @Override
   public void run(String... args) throws Exception {
      log.info("profiles: " + devProfile);
   }
}
