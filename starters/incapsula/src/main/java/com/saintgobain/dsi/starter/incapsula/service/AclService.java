package com.saintgobain.dsi.starter.incapsula.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.bean.AclsConfiguration;
import com.saintgobain.dsi.starter.incapsula.bean.AclsRules;
import com.saintgobain.dsi.starter.incapsula.bean.Continents;
import com.saintgobain.dsi.starter.incapsula.bean.Countries;
import com.saintgobain.dsi.starter.incapsula.bean.UrlPatterns;
import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class AclService extends IncapsulaService {

    private final static String PREFIX_API_ACL = "api.acl.";

    public AclService(RestTemplate incapsulaRestTemplate) {
        super(incapsulaRestTemplate);
    }

    public IncapsulaResponse configureAcl(String endpoint, String siteId, @Valid AclsConfiguration conf)
            throws IncapsulaException {

        AclsRules rule = conf.getRuleId();

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);
        uriComponentsBuilder.queryParam("rule_id", PREFIX_API_ACL + rule.name());

        switch (rule) {

        case blacklisted_urls:
            uriComponentsBuilder = configureBlacklistedUrls(uriComponentsBuilder, conf);
            break;

        case blacklisted_countries:
            uriComponentsBuilder = configureBlacklistedCountries(uriComponentsBuilder, conf);
            break;

        case blacklisted_ips:
            uriComponentsBuilder = configureIps(uriComponentsBuilder, conf);
            break;

        case whitelisted_ips:
            uriComponentsBuilder = configureIps(uriComponentsBuilder, conf);
            break;
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;

    }

    private UriComponentsBuilder configureBlacklistedUrls(UriComponentsBuilder uriComponentsBuilder,
            AclsConfiguration conf) throws IncapsulaException {

        List<String> urls = conf.getUrls();

        if (CollectionUtils.isEmpty(urls)) {
            uriComponentsBuilder.queryParam("urls", "");
        } else {

            List<UrlPatterns> urlPatterns = conf.getUrlPatterns();

            if (CollectionUtils.isEmpty(urlPatterns)) {
                throw new BadRequestException("Urls pattern has to be set if Urls param is present");
            }

            String urlsParam = urls.stream().map(s -> s).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("urls", urlsParam);

            String urlPatternsParam = urlPatterns.stream().map(p -> p.name()).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("url_patterns", urlPatternsParam);
        }

        return uriComponentsBuilder;
    }

    private UriComponentsBuilder configureBlacklistedCountries(UriComponentsBuilder uriComponentsBuilder,
            AclsConfiguration conf) {

        List<Countries> countries = conf.getCountries();

        List<Continents> continents = conf.getContinents();

        if (CollectionUtils.isEmpty(countries)) {
            uriComponentsBuilder.queryParam("countries", "");
        } else {
            String countriesParam = countries.stream().map(c -> c.name()).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("countries", countriesParam);
        }

        if (CollectionUtils.isEmpty(continents)) {
            uriComponentsBuilder.queryParam("continents", "");
        } else {
            String continentsParam = continents.stream().map(c -> c.name()).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("continents", continentsParam);
        }
        return uriComponentsBuilder;
    }

    private UriComponentsBuilder configureIps(UriComponentsBuilder uriComponentsBuilder,
            AclsConfiguration conf) {

        List<String> ips = conf.getIps();

        if (CollectionUtils.isEmpty(ips)) {
            uriComponentsBuilder.queryParam("ips", "");
        } else {
            String ipsParam = ips.stream().map(ip -> ip).collect(Collectors.joining(","));
            uriComponentsBuilder.queryParam("ips", ipsParam);
        }

        return uriComponentsBuilder;
    }
}
