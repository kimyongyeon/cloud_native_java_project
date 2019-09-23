package com.tony.msaarchitecturecoffeeorder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeOrderRestController {

    @HystrixCommand(fallbackMethod = "fallbackFunction")
    @RequestMapping(value = "/coffeeOrder/{name}", method = RequestMethod.POST)
    public String coffeeOrder(@PathVariable("name") String name) {

        if (name.equals("error")) {
            throw new RuntimeException("error");
        }

        return "coffeeOrder OK";
    }

    public String fallbackFunction(@PathVariable("name") String name) { return "fallbackFunction()"; }

}
