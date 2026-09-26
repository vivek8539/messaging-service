package com.connectify.messaging_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    private RedisMessageService redisMessageService;

    public void sendMsg(String receiverUserId) {
        redisMessageService.readMessage(receiverUserId, ReadOffset.lastConsumed(), 5);
    }

    public void sendPendingMessage(String receiverUserId, int pendingMessageNumber) {
        redisMessageService.readMessage(receiverUserId, ReadOffset.from("0"), pendingMessageNumber);
    }


}
