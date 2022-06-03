package com.saintgobain.dsi.starter.qualys.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * The type Saint gobain api qualys properties.
 */
@Data
@ConfigurationProperties(prefix = "saint.gobain.api.qualys", ignoreUnknownFields = false)
public class SaintGobainApiQualysProperties {

    private String url;

    private String username;

    private String password;
}
