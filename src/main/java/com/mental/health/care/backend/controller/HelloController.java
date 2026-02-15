package com.mental.health.care.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Spring Boot!";
    }

    @GetMapping("/greet")
    public String greet() {
        return "Welcome to the Mental Health Care Backend!";
    }
}
