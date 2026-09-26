package com.connectify.messaging_service.websocket.handler;

import com.connectify.messaging_service.model.Message;
import com.connectify.messaging_service.service.DeliveryService;
import com.connectify.messaging_service.service.RedisMessageService;
import com.connectify.messaging_service.websocket.registry.SessionRegistry;
import com.google.gson.Gson;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class TestWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private RedisMessageService redisMessageService;
    @Autowired
    private DeliveryService deliveryService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessionRegistry.addSession(session.getAttributes().get("userId").toString(), session);
        //check user have any unsend msg
        int pendingMessageNumber = redisMessageService.hasPendingMessage(session.getAttributes().get("userId").toString());
        if (pendingMessageNumber > 0) {
            deliveryService.sendPendingMessage(session.getAttributes().get("userId").toString(), pendingMessageNumber);
        }

        //if have unsend msg then send all msg
    }

    @Override
    public void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) throws IOException {

        // A send msg B, A establish connection with server
            String payload = message.getPayload();
            Message userMessage = new Gson().fromJson(payload, Message.class);
            redisMessageService.storeMessage(userMessage);
            //check if user B websocket connection is avai;lable if available then
            deliveryService.sendMsg(userMessage.getReceiver().getUserId());
        //subscribe redis inbox stream for the session user

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionRegistry.removeSession(session.getAttributes().get("userId").toString());
        //unsubscribe redis  inbox stream for the session user
    }

}
