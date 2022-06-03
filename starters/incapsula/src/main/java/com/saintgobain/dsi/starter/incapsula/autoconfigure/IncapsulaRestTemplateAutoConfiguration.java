package com.saintgobain.dsi.starter.incapsula.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableConfigurationProperties(SaintGobainApiIncapsulaProperties.class)
public class IncapsulaRestTemplateAutoConfiguration {

    private final SaintGobainApiIncapsulaProperties properties;

    /**
     * Instantiates a new Rest template configuration.
     *
     * @param properties the properties
     */
    public IncapsulaRestTemplateAutoConfiguration(SaintGobainApiIncapsulaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate incapsulaRestTemplate(RestTemplateBuilder builder) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(properties.getUrl());

        RestTemplate incapsulaRestTemplate = builder.rootUri(uriComponentsBuilder.toUriString())
                .build();

        incapsulaRestTemplate.getInterceptors().add((request, body, execution) -> {

            if (!request.getURI().toString().contains("api_id")) {
                UriComponentsBuilder uri = UriComponentsBuilder
                        .fromUri(request.getURI())
                        .queryParam("api_id", properties.getApiId())
                        .queryParam("api_key", properties.getApiKey());

                HttpComponentsClientHttpRequestFactory wrapper = new HttpComponentsClientHttpRequestFactory();

                HttpRequest newRequest = wrapper.createRequest(uri.build().toUri(), request.getMethod());

                return execution.execute(newRequest, body);
            } else {
                return execution.execute(request, body);
            }

        });

        return incapsulaRestTemplate;
    }

}
