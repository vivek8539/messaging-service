package com.connectify.messaging_service.service;

import com.connectify.messaging_service.model.Message;
import com.connectify.messaging_service.model.User;
import com.connectify.messaging_service.websocket.registry.SessionRegistry;
import com.google.gson.Gson;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RedisMessageService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SessionRegistry sessionRegistry;

    public void storeMessage(Message message) {
        //create chat msg stream
        Map<String, String> msg = new HashMap<>();
        msg.put("senderUserId", message.getSender().getUserId());
        msg.put("receiverUserId", message.getReceiver().getUserName());
        msg.put("message",  message.getMessage());
        String key = createKeyName(message.getReceiver().getUserId());
        if (!stringRedisTemplate.hasKey(key)) { //TODO when user register for the first time then only create the stream and attach group
            stringRedisTemplate.opsForStream().createGroup(key, ReadOffset.latest(),"chatGroup");
        }

        stringRedisTemplate.opsForStream().add(key, msg);
    }

    public void readMessage(String receiverUserId, ReadOffset readOffset, int count) {
        List<@NonNull MapRecord<String, Object, Object>> read = stringRedisTemplate.opsForStream().read(
                Consumer.from("chatGroup", "delivery"),
                StreamReadOptions.empty().count(count),
                StreamOffset.create(createKeyName(receiverUserId), readOffset)
        );
        read.forEach(record -> {

            try {
                WebSocketSession session = sessionRegistry.getSession(receiverUserId);
                if (Objects.nonNull(session)) {

                    session.sendMessage(new TextMessage(new Gson().toJson(createMessage(record.getValue()))));
                    stringRedisTemplate.opsForStream().acknowledge(createKeyName(receiverUserId), "chatGroup", record.getId());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }


    private Message createMessage(Map<Object, Object> entry) {
        return Message.builder()
                .sender(User.builder()
                        .userId(entry.get("senderUserId").toString()).build())
                .receiver(User.builder()
                        .userId(entry.get("receiverUserId").toString()).build())
                .message(entry.get("message").toString())
                .build();
    }

    private String createKeyName(String receiverUserId) {
        return "chat:" + receiverUserId;
    }

    public int hasPendingMessage(String userId) {
        String key = createKeyName(userId);
        if (!stringRedisTemplate.hasKey(key)) { //TODO when user register for the first time then only create the stream and attach group
            stringRedisTemplate.opsForStream().createGroup(key, ReadOffset.latest(), "chatGroup");
        }
        Consumer consumer = Consumer.from("chatGroup", "delivery");
        PendingMessagesSummary pendingMessagesSummary = stringRedisTemplate.opsForStream().pending(createKeyName(userId), "chatGroup");
        Long pendingMsgCount = pendingMessagesSummary.getPendingMessagesPerConsumer().get("delivery");
        return pendingMsgCount == null ? 0: pendingMsgCount.intValue();

    }

}
