package com.ecommerce.demo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {

    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
