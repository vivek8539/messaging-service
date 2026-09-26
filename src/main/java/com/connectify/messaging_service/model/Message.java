package com.connectify.messaging_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    private String message;
    private User sender;
    private User receiver;
}
