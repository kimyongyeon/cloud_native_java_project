package com.tony.democlientsideloadbalancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscoveryClientRunner implements ApplicationRunner {

   private final DiscoveryClient discoveryClient;

   // <1>
   DiscoveryClientRunner(DiscoveryClient discoveryClient) {
      this.discoveryClient = discoveryClient;
   }

   private void logServiceInstance(ServiceInstance si) {
      String msg = String.format("host = %s, port = %s, service ID = %s",
         si.getHost(), si.getPort(), si.getServiceId());
      log.info(msg);
   }

   @Override
   public void run(ApplicationArguments args) throws Exception {
      // <2>
      String serviceId = "greetings-service";
      log.info(String.format("DiscoveryClientRunner registered instances of '%s'", serviceId));
      this.discoveryClient.getInstances(serviceId)
         .forEach(this::logServiceInstance);
   }

}
