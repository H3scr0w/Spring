package com.saintgobain.dsi.starter.incapsula.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

public class IncapsulaService {

    protected final RestTemplate incapsulaRestTemplate;

    public IncapsulaService(RestTemplate incapsulaRestTemplate) {
        this.incapsulaRestTemplate = incapsulaRestTemplate;
    }

    protected UriComponentsBuilder initRequest(String endpoint) throws IncapsulaException {

        if (StringUtils.isBlank(endpoint)) {
            throw new BadRequestException("Endpoint has to be set");
        }

        return UriComponentsBuilder.fromUriString(endpoint);
    }

    protected UriComponentsBuilder initRequest(String endpoint, String siteId) throws IncapsulaException {

        if (StringUtils.isAnyBlank(endpoint, siteId)) {
            throw new BadRequestException("Endpoint and SiteId have to be set");
        }

        return UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("site_id", siteId);
    }

    protected ResponseEntity<IncapsulaResponse> postRequest(UriComponentsBuilder uriComponentsBuilder) {
        return incapsulaRestTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, null,
                IncapsulaResponse.class);
    }

    protected ResponseEntity<IncapsulaResponse> postRequest(UriComponentsBuilder uriComponentsBuilder,
            MultiValueMap<String, String> data) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        return incapsulaRestTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, request,
                IncapsulaResponse.class);

    }

    protected ResponseEntity<IncapsulaResponse> postRequestPaginated(UriComponentsBuilder uriComponentsBuilder,
            Pageable pageable) {

        uriComponentsBuilder.queryParam("page_size", pageable.getPageSize());
        uriComponentsBuilder.queryParam("page_num", pageable.getPageNumber());

        return incapsulaRestTemplate.exchange(uriComponentsBuilder.toUriString(),
                HttpMethod.POST, null,
                IncapsulaResponse.class);

    }

}
