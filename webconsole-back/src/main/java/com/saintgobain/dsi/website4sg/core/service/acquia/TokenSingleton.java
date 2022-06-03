package com.saintgobain.dsi.website4sg.core.service.acquia;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenSingleton {

    private TokenData tokenData;

    @Bean
    public TokenData getTokenData() {
        return this.tokenData;
    }

    public synchronized void setTokenData(TokenData tokenData) {
        this.tokenData = tokenData;
    }
}
