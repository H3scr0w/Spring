package com.sgdbf.service.notifier.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * The type Web socket config.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // boost messages delivery to clients
        registration
                // The default value is 10 seconds (i.e. 10 * 1000).
                .setSendTimeLimit(60 * 1000)
                // The default value is 512K (i.e. 512 * 1024).
                .setSendBufferSizeLimit(200 * 1024 * 1024)
                // The default value is 64K (i.e. 64 * 1024).
                .setMessageSizeLimit(128 * 1024);
    }


    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // boost messages consumption from clients side if slow bandwith in network
        // default value is the twice number of available processors (e.g. 2 x 1)
        registration.taskExecutor().corePoolSize(100).maxPoolSize(100);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint(
                "/notifier/subscribe",
                "/subscribe")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


}
