package com.sample.cloud.eurekaclient.demoeurekaclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="greetings-service")
public interface GreetingsClient {
   @RequestMapping(method= RequestMethod.GET, value="/hi/{name}")
   String hi(@PathVariable("name") String name);
}
