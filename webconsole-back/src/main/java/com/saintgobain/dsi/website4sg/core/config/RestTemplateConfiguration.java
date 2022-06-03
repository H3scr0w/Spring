package com.saintgobain.dsi.website4sg.core.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.website4sg.core.service.acquia.TokenSingleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Rest template configuration.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final SaintGobainProperties properties;

    private final TokenSingleton tokenSingleton;

    /**
     * Rundeck Rest template rest template.
     *
     * @param builder the builder
     * @return the rest template
     */
    @Bean
    public RestTemplate rundeckRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(properties.getApi().getRundeck().getUrl()).additionalInterceptors((request, body,
                execution) -> {
            request.getHeaders().set("X-Rundeck-Auth-Token", properties.getApi().getRundeck().getToken());
            return execution.execute(request, body);
        }).build();
    }

    @Bean
    public RestTemplate repositoryRestTemplate(RestTemplateBuilder builder) {

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        RestTemplate repositoryRestTemplate = builder.rootUri(properties.getApi().getNexus().getUrl())
                .additionalInterceptors((request, body,
                        execution) -> {
                    request.getHeaders()
                            .set(HttpHeaders.AUTHORIZATION, properties.getApi().getNexus().getBasicHeader());
                    request.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                    log.info("URI:" + request.getURI().toString());
                    log.info("HOST:" + request.getURI().getHost());
                    log.info("PATH:" + request.getURI().getPath());
                    log.info("BODY:" + new String(body, StandardCharsets.UTF_8));
                    log.info("CONTENT-TYPE:" + request.getHeaders().getContentType().toString());
                    log.info("ACCEPT:" + request.getHeaders().getAccept().stream().map(n -> n.toString()).collect(
                            Collectors
                                    .joining(",")));
                    log.info("METHOD:" + request.getMethodValue());
                    return execution.execute(request, body);
                }).build();

        repositoryRestTemplate.setRequestFactory(requestFactory);

        return repositoryRestTemplate;
    }

    @Bean
    public RestTemplate openDjRestTemplate(RestTemplateBuilder builder) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate opendjRestTemplate = builder.rootUri(properties.getApi().getOpenDj().getPath())
                .additionalInterceptors((request, body,
                        execution) -> {
                    request.getHeaders()
                            .set(HttpHeaders.AUTHORIZATION, properties.getApi().getOpenDj().getBasicHeader());
                    return execution.execute(request, body);
                }).build();

        opendjRestTemplate.setRequestFactory(requestFactory);

        return opendjRestTemplate;

    }

    @Bean
    public RestTemplate acquiaTokenRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(properties.getApi().getAcquia().getTokenUrl()).additionalInterceptors((request, body,
                execution) -> {
            return execution.execute(request, body);
        }).build();
    }

    @Bean
    public RestTemplate acquiaApiRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(properties.getApi().getAcquia().getApiUrl()).additionalInterceptors((request, body,
                execution) -> {

            if (tokenSingleton.getTokenData() != null) {
                request.getHeaders()
                        .set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenSingleton.getTokenData().getAccessToken());
            }

            return execution.execute(request, body);
        }).build();
    }

}
