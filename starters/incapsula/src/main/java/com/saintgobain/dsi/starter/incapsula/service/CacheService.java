package com.saintgobain.dsi.starter.incapsula.service;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.bean.CacheMode;
import com.saintgobain.dsi.starter.incapsula.bean.CacheModes;
import com.saintgobain.dsi.starter.incapsula.bean.CacheRules;
import com.saintgobain.dsi.starter.incapsula.bean.CacheSettings;
import com.saintgobain.dsi.starter.incapsula.bean.DurationPeriod;
import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class CacheService extends IncapsulaService {

    public CacheService(RestTemplate incapsulaRestTemplate) {
        super(incapsulaRestTemplate);
    }

    public IncapsulaResponse purgeCache(String endpoint, String siteId) throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse configureCacheMode(String endpoint, String siteId, @Valid CacheMode cacheMode)
            throws IncapsulaException {

        CacheModes mode = cacheMode.getCacheMode();
        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);
        uriComponentsBuilder.queryParam("cache_mode", mode.name());

        switch (mode) {

        case aggressive:

            Integer aggressiveCacheDuration = cacheMode.getAggressiveCacheDuration();

            DurationPeriod aggressivePeriod = cacheMode.getDurationPeriod();

            if (aggressiveCacheDuration != null) {

                if (aggressivePeriod != null) {
                    uriComponentsBuilder.queryParam("aggressive_cache_duration", aggressiveCacheDuration + "_"
                            + aggressivePeriod);
                } else {
                    throw new BadRequestException(
                            "DurationPeriod has to be set if aggressiveCacheDuration param is present");
                }

            }

            break;

        case static_and_dynamic:

            Integer dynamicCacheDuration = cacheMode.getDynamicCacheDuration();

            DurationPeriod dynamicPeriod = cacheMode.getDurationPeriod();

            if (dynamicCacheDuration != null) {

                if (dynamicPeriod != null) {
                    uriComponentsBuilder.queryParam("dynamic_cache_duration", dynamicCacheDuration + "_"
                            + dynamicPeriod);
                } else {
                    throw new BadRequestException(
                            "DurationPeriod has to be set if dynamicCacheDuration param is present");
                }

            }
            break;

        default:
            break;
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse configureCacheRules(String endpoint, String siteId, CacheRules cacheRules)
            throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        if (!CollectionUtils.isEmpty(cacheRules.getCacheHeaders())) {

            String cacheHeadersParam = cacheRules.getCacheHeaders().stream().map(s -> s).collect(Collectors.joining(
                    ","));
            uriComponentsBuilder.queryParam("cache_headers", cacheHeadersParam);
        }

        // Configure Always Cache Rules

        if (!CollectionUtils.isEmpty(cacheRules.getAlwaysCacheResourceUrl())) {

            String alwaysCacheResourceUrlParam = cacheRules.getAlwaysCacheResourceUrl().stream().map(s -> s).collect(
                    Collectors.joining(
                            ","));
            uriComponentsBuilder.queryParam("always_cache_resource_url", alwaysCacheResourceUrlParam);
        }

        if (!CollectionUtils.isEmpty(cacheRules.getAlwaysCacheResourcePattern())) {

            String alwaysCacheResourcePatternParam = cacheRules.getAlwaysCacheResourcePattern().stream().map(s -> s
                    .name())
                    .collect(
                            Collectors.joining(
                                    ","));
            uriComponentsBuilder.queryParam("always_cache_resource_pattern", alwaysCacheResourcePatternParam);
        }

        if (cacheRules.getAlwaysCacheResourceDuration() != null) {
            if (cacheRules.getDurationPeriod() != null) {

                Integer alwaysCacheResourceDuration = cacheRules.getAlwaysCacheResourceDuration();
                DurationPeriod period = cacheRules.getDurationPeriod();

                uriComponentsBuilder.queryParam("always_cache_resource_duration", alwaysCacheResourceDuration + "_"
                        + period);
            } else {
                throw new BadRequestException(
                        "DurationPeriod has to be set if alwaysCacheResourceDuration param is present");
            }
        }

        // Configure Never Cache Rules

        if (!CollectionUtils.isEmpty(cacheRules.getNeverCacheResourceUrl())) {
            String neverCacheResourceUrlParam = cacheRules.getNeverCacheResourceUrl().stream().map(s -> s).collect(
                    Collectors.joining(
                            ","));
            uriComponentsBuilder.queryParam("never_cache_resource_url", neverCacheResourceUrlParam);
        }

        if (!CollectionUtils.isEmpty(cacheRules.getNeverCacheResourcePattern())) {

            String neverCacheResourcePatternParam = cacheRules.getNeverCacheResourcePattern().stream().map(s -> s
                    .name())
                    .collect(
                            Collectors.joining(
                                    ","));
            uriComponentsBuilder.queryParam("never_cache_resource_pattern", neverCacheResourcePatternParam);
        }

        // Configure Clear Cache Rules

        if (cacheRules.getClearAlwaysCacheRules() != null) {
            uriComponentsBuilder.queryParam("clear_always_cache_rules", cacheRules.getClearAlwaysCacheRules());
        }

        if (cacheRules.getClearNeverCacheRules() != null) {
            uriComponentsBuilder.queryParam("clear_never_cache_rules", cacheRules.getClearNeverCacheRules());
        }

        if (cacheRules.getClearCacheHeadersRules() != null) {
            uriComponentsBuilder.queryParam("clear_cache_headers_rules", cacheRules.getClearCacheHeadersRules());
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse configureCacheSettings(String endpoint, String siteId, @Valid CacheSettings cacheSettings)
            throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);
        uriComponentsBuilder.queryParam("param", cacheSettings.getParam().name());
        uriComponentsBuilder.queryParam("value", cacheSettings.getValue());

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }
}
