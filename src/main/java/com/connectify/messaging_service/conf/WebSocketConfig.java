package com.connectify.messaging_service.conf;

import com.connectify.messaging_service.websocket.UserIdInterceptor;
import com.connectify.messaging_service.websocket.handler.TestWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final TestWebSocketHandler testWebSocketHandler;
    private  final UserIdInterceptor  userIdInterceptor;

    public WebSocketConfig(TestWebSocketHandler testWebSocketHandler, UserIdInterceptor userIdInterceptor) {
        this.testWebSocketHandler = testWebSocketHandler;
        this.userIdInterceptor = userIdInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testWebSocketHandler, "/ws")
                .addInterceptors(userIdInterceptor)
                .setAllowedOrigins("*");
    }
}
