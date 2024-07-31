package com.example.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 配置WebSocket和STOMP协议的类。
 * 此配置类定义了应用程序如何处理WebSocket连接和消息代理。
 *
 * @author russ2
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理，设置消息的路由和目的地。
     * 在这里，我们启用了一个简单的消息代理，并设置其前缀为"/topic"，
     * 表示所有以"/topic"开头的目的地都将由代理处理。
     * 同时，我们设置了应用程序消息目的地的前缀为"/app"，
     * 这意味着所有发往"/app"开头目的地的消息将由我们的控制器处理。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 注册STOMP端点，即WebSocket的接入点。
     * 我们在这里添加了一个端点"/ws"，并使用SockJS作为WebSocket的回退机制。
     * SockJS是一种用于浏览器的库，它提供了WebSocket的polyfill，
     * 使得在不支持WebSocket的旧浏览器中也能使用类似WebSocket的功能。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}
