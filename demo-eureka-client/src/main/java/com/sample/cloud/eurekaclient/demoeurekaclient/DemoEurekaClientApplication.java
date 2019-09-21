package com.sample.cloud.eurekaclient.demoeurekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@Slf4j
public class DemoEurekaClientApplication {

//   private final RestTemplate restTemplate;
//
//   @Autowired
//   DemoEurekaClientApplication(
//      @LoadBalanced RestTemplate restTemplate) { // <1>
//      this.restTemplate = restTemplate;
//   }

   private final GreetingsClient greetingsClient;

   @Autowired
   DemoEurekaClientApplication(GreetingsClient greetingsClient) {
      this.greetingsClient = greetingsClient;
   }

   public static void main(String[] args) {
      SpringApplication.run(DemoEurekaClientApplication.class, args);
   }

   @RestController
   class GreetingsRestController {

      @GetMapping("/feign/{name}")
      String feign(@PathVariable String name) {
         return greetingsClient.hi(name);
      }

//      @RequestMapping(method = RequestMethod.GET, value="/hi/{name}")
//      String hi(@PathVariable String name) {
//
//         ResponseEntity<String> responseEntity = restTemplate
//            .exchange("http://greetings-service/hi/{name}", HttpMethod.GET, null,
//               String.class, name);
//         return "hi:" + responseEntity.getBody().toString();
//      }

   }

}
