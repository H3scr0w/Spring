package com.saintgobain.dsi.starter.incapsula.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "saint.gobain.api.incapsula", ignoreUnknownFields = false)
public class SaintGobainApiIncapsulaProperties {

    private String url;

    private String apiId;

    private String apiKey;
}
