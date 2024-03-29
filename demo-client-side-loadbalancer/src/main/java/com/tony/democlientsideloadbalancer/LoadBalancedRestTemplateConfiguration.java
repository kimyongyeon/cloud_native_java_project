package com.tony.democlientsideloadbalancer;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LoadBalancedRestTemplateConfiguration {

   @Bean
   @LoadBalanced
   RestTemplate restTemplate() {
      return new RestTemplateBuilder().build();
   }
}
