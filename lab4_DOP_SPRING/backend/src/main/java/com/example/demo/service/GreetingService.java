package com.example.demo.service;

public class GreetingService implements MessageService {
    @Override
    public void sendMessage(String recipient) {
        System.out.println("hello to  " + recipient);
    }
}
