package com.tony.democlientsideloadbalancer;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

//@Component
@Slf4j
public class LoadBalancedWebClientRunner implements ApplicationRunner {

   private final WebClient webClient;

   LoadBalancedWebClientRunner(@LoadBalanced WebClient client) {
      this.webClient = client;
   }

   @Override
   public void run(ApplicationArguments args) throws Exception {
      Map<String, String> variables = Collections.singletonMap("name",
         "Cloud Natives!");

      // <2> : 못찾는다... 쓰지마 역시 인터넷은 믿을게 못됨.
      this.webClient.get().uri("hi/{name}", variables)
         .retrieve().bodyToMono(JsonNode.class).map(x -> x.get("greeting").asText())
         .subscribe(greeting -> log.info("greeting: " + greeting));


   }
}
