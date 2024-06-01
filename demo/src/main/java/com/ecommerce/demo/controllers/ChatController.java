package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @MessageMapping(value = "/chat")
    @SendTo(value = "/topic/public")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage){
        return chatMessage;
    }

    @MessageMapping(value = "/chat.addUser")
    @SendTo(value = "/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        //Adds username in web socket session
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }


}
