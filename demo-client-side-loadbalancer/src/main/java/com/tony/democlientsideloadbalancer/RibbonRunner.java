package com.tony.democlientsideloadbalancer;

import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class RibbonRunner implements ApplicationRunner {
   private final DiscoveryClient discoveryClient;

   public RibbonRunner(DiscoveryClient discoveryClient) {
      this.discoveryClient = discoveryClient;
   }

   @Override
   public void run(ApplicationArguments args) throws Exception {
      String serviceId = "greetings-service";

      // <1>
      List<Server> servers = this.discoveryClient.getInstances(serviceId).stream()
         .map(si -> new Server(si.getHost(), si.getPort()))
         .collect(Collectors.toList());

      // <2>
      IRule roundRobinRule = new RoundRobinRule();

      BaseLoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder()
         .withRule(roundRobinRule).buildFixedServerListLoadBalancer(servers);

      IntStream.range(0, 10).forEach(i -> {
         // <3>
         Server server = loadBalancer.chooseServer();
         URI uri = URI.create("http://" + server.getHost() + ":" + server.getPort()
            + "/");
         log.info("RibbonRunner resolved service " + uri.toString());
      });
   }
}
