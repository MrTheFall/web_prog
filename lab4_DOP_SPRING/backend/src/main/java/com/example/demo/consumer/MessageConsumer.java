package com.example.demo.consumer;

import com.example.demo.service.MessageService;

public class MessageConsumer {
    private final MessageService messageService;

    public MessageConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    public void processMessages(String recipient) {
        this.messageService.sendMessage(recipient);
    }
}