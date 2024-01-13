package com.essexboy;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    MeterRegistry registry;

    @GetMapping("/hello")
    public String hello() {
        registry.counter("hello.counter").increment();
        return "hello";
    }
}
