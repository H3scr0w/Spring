package com.saintgobain.dsi.website4sg.core.service.acquia;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.website4sg.core.config.SaintGobainProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenSingleton tokenSingleton;
    private final SaintGobainProperties properties;
    private final RestTemplate acquiaTokenRestTemplate;

    public void getAccessToken() {

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id", properties.getApi().getAcquia().getApiKey());
        data.add("client_secret", properties.getApi().getAcquia().getApiSecret());
        data.add("grant_type", "client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        ResponseEntity<TokenData> response = acquiaTokenRestTemplate.exchange("/auth/oauth/token",
                HttpMethod.POST, request,
                TokenData.class);

        if (response != null) {
            tokenSingleton.setTokenData(response.getBody());
        }

    }

}
