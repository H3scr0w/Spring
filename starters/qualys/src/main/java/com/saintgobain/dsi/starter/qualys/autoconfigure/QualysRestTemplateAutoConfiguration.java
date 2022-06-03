package com.saintgobain.dsi.starter.qualys.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableConfigurationProperties(SaintGobainApiQualysProperties.class)
public class QualysRestTemplateAutoConfiguration {

    private final SaintGobainApiQualysProperties properties;

    /**
     * Instantiates a new Rest template configuration.
     *
     * @param properties the properties
     */
    public QualysRestTemplateAutoConfiguration(SaintGobainApiQualysProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate qualysRestTemplate(RestTemplateBuilder builder) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(properties.getUrl());

        RestTemplate qualysRestTemplate = builder.rootUri(uriComponentsBuilder.toUriString())
                .build();

        qualysRestTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(
                        properties.getUsername(),
                        properties.getPassword()));
        return qualysRestTemplate;
    }

}
