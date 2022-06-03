package com.sgdbf.service.notifier.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.config.ChannelRegistration;


@Configuration
@Scope( proxyMode = ScopedProxyMode.NO)
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    public void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10);
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
