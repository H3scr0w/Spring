package com.saintgobain.dsi.starter.incapsula.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.bean.Site;
import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class SiteService extends IncapsulaService {

    public SiteService(RestTemplate incapsulaRestTemplate) {
        super(incapsulaRestTemplate);
    }

    public IncapsulaResponse addSite(String endpoint, Site site) throws IncapsulaException {

        String domain = site.getDomain();

        if (StringUtils.isBlank(domain)) {
            throw new BadRequestException("Site domain has to be set");
        }

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint);

        uriComponentsBuilder.queryParam("domain", domain);

        if (site != null && !CollectionUtils.isEmpty(site.getSiteIp())) {
            String siteIpParam = site.getSiteIp().stream().map(s -> s).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("site_ip", siteIpParam);
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse getStatus(String endpoint, String siteId, Site site) throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        if (site != null && !CollectionUtils.isEmpty(site.getTests())) {
            String testsParam = site.getTests().stream().map(s -> s.name()).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("tests", testsParam);
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;

    }

    public IncapsulaResponse configureSite(String endpoint, String siteId, Site site) throws IncapsulaException {
        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        if (site != null && !CollectionUtils.isEmpty(site.getSiteIp())) {
            uriComponentsBuilder = configureSiteIp(uriComponentsBuilder, site.getSiteIp());
        } else if (site != null && site.getRemoveSsl() != null) {
            uriComponentsBuilder = configureSsl(uriComponentsBuilder, site.getRemoveSsl());
        } else {
            throw new BadRequestException("Site Ip or Ssl parameter has to be set");
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;

    }

    private UriComponentsBuilder configureSiteIp(UriComponentsBuilder uriComponentsBuilder, List<String> siteIp) {

        String siteIpParam = siteIp.stream().map(s -> s).collect(Collectors.joining(","));
        uriComponentsBuilder.queryParam("param", "site_ip");
        uriComponentsBuilder.queryParam("value", siteIpParam);
        return uriComponentsBuilder;

    }

    private UriComponentsBuilder configureSsl(UriComponentsBuilder uriComponentsBuilder, Boolean removeSsl) {

        uriComponentsBuilder.queryParam("param", "remove_ssl");
        uriComponentsBuilder.queryParam("value", removeSsl.toString());
        return uriComponentsBuilder;
    }
}
