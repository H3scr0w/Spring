package com.saintgobain.dsi.website4sg.core.service.acquia;

import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnvironmentsService {

    private final RestTemplate acquiaApiRestTemplate;

    private final TokenService tokenService;

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 500))
    public void clearVarnishDomain(String environmentId, String domain) {

        try {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/environments/")
                    .path(environmentId)
                    .path("/domains/")
                    .path(domain)
                    .path("/actions/clear-varnish");
            acquiaApiRestTemplate.exchange(uriComponentsBuilder.toUriString(),
                    HttpMethod.POST, null,
                    AcquiaResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 401 || e.getRawStatusCode() == 403) {
                log.warn("acquia token error: ", e.getResponseBodyAsString());
                tokenService.getAccessToken();
            }
            throw e;

        }
    }

}
