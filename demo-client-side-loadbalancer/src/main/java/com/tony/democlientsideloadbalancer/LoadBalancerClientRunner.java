package com.tony.democlientsideloadbalancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Slf4j
public class LoadBalancerClientRunner implements ApplicationRunner {

   private final LoadBalancerClient loadBalancerClient;

   LoadBalancerClientRunner(LoadBalancerClient loadBalancerClient) {
      this.loadBalancerClient = loadBalancerClient;
   }

   @Override
   public void run(ApplicationArguments args) throws Exception {
      LoadBalancerRequest<URI> loadBalancerRequest = server -> URI.create("http://"
         + server.getHost() + ":" + server.getPort() + "/");
      URI uri = this.loadBalancerClient.execute("greetings-service",
         loadBalancerRequest);
      log.info("LoadBalancerClientRunner resolved service " + uri.toString());
   }
}
