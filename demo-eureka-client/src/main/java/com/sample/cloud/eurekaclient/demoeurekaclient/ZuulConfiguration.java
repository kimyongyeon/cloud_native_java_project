package com.sample.cloud.eurekaclient.demoeurekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableZuulProxy
public class ZuulConfiguration {
   @Bean
   CommandLineRunner commandLineRunner(RouteLocator routeLocator) {
      return arg ->
         routeLocator
            .getRoutes()
            .forEach(r->
               log.info(String.format("%s (%s) %s",
                  r.getId(), r.getLocation(), r.getFullPath())));

   }
}


