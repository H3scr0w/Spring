package com.saintgobain.dsi.pcpeg.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.RequiredArgsConstructor;

/**
 * The type Rest template configuration.
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private static final String HEADER_JWT = "jwt";

    private final PcpegProperties properties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.rootUri(properties.getDirectory().getUrl())
                .additionalInterceptors((request, body, execution) -> {
                    final String jwtHeader = getCurrentRequest().getHeader(HEADER_JWT);
                    if (StringUtils.isNotBlank(jwtHeader)) {
                        request.getHeaders().set(HEADER_JWT, jwtHeader);
                    }
                    request.getHeaders().set("KeyId", properties.getDirectory().getKeyId());
                    return execution.execute(request, body);
                }).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }
}
