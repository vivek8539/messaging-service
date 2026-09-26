package com.connectify.messaging_service.controller;

import com.connectify.messaging_service.websocket.registry.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RestController("api/v1/msg")
public class MessageController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping
    public String sendMsg(String userId) throws IOException {
        WebSocketSession session = sessionRegistry.getSession(userId);
        session.sendMessage(new TextMessage("Hello"));
        return "Success";
    }
}
