package com.example.AreaChecker.service;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class GreetingService {
    public String greet(String name) {
        return "Hello, " + name;
    }
}
