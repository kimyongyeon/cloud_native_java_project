package com.sample.cloud.eurekaclient.demoeurekaclient;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("throttled")
@Configuration
public class ThrottlingConfiguration {

   /**
    * 초당 0.1개 즉, 10초에 1개의 요청만을 허용
    * @return
    */
   @Bean
   RateLimiter rateLimiter() {
      return RateLimiter.create(1.0D / 10.0D);
   }
}
