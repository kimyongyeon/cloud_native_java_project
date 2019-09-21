package com.sample.cloud.eurekaclient.demoeurekaclient;

import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@RestController
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

   @Autowired
   LoadBalancerClient loadBalancerClient;

   /**
    * /greetings-client-uri 종단점은 다운스트림 서비스의 greetings-client 인스턴스의 종단점을 반환한다.
    * 이 종단점은 엣지 서비스의 CorsFilter에서 Access-Control-* 헤더를 붙여준 덕분에 다른 오리진에서도 접근할 수 있다.
    * 그리고 LoadBalancerClient를 이용하면 로드밸런ㄴ서 설정에 따라 인스턴스 정보를 얻어올 수 있다.
    * 클라이언트 로드밸런서는 기본값으로 넷플릭스 리본을 사용하는데 리본은 기본적으로 라운드로빈 로드밸런싱을 사용한다.
    * @return
    * @throws Exception
    */
   @GetMapping(value = "/greetings-client-uri", produces = MediaType.APPLICATION_JSON_VALUE)
   Map<String, String> greetingsClientURI() throws Exception {
      return Optional
         .ofNullable(loadBalancerClient.choose("greetings-service"))
         .map(si -> Collections.singletonMap("uri", si.getUri().toString())) // json 데이터 출력
         .orElse(null);
   }

   @GetMapping("/feign/{name}")
   String feign(@PathVariable String name) {
      return greetingsClient.hi(name);
   }

//   class GreetingsRestController {
//      @RequestMapping(method = RequestMethod.GET, value="/hi/{name}")
//      String hi(@PathVariable String name) {
//
//         ResponseEntity<String> responseEntity = restTemplate
//            .exchange("http://greetings-service/hi/{name}", HttpMethod.GET, null,
//               String.class, name);
//         return "hi:" + responseEntity.getBody().toString();
//      }
//   }

}
