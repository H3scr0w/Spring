package com.saintgobain.dsi.starter.incapsula.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.incapsula.bean.CacheMode;
import com.saintgobain.dsi.starter.incapsula.bean.CacheModes;
import com.saintgobain.dsi.starter.incapsula.bean.CacheRules;
import com.saintgobain.dsi.starter.incapsula.bean.CacheSettingParams;
import com.saintgobain.dsi.starter.incapsula.bean.CacheSettings;
import com.saintgobain.dsi.starter.incapsula.bean.DurationPeriod;
import com.saintgobain.dsi.starter.incapsula.bean.UrlPatterns;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
public class CacheServiceTest {

    @Mock
    private RestTemplate incapsulaRestTemplate;

    private CacheService cacheService;

    @Before
    public void setUp() throws Exception {

        cacheService = new CacheService(incapsulaRestTemplate);
    }

    @Test
    public void purgeCacheTest() throws IncapsulaException {

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/cache/purge?site_id=41472615";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = cacheService.purgeCache("/api/prov/v1/sites/cache/purge", siteId);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void configureCacheModeTest() throws IncapsulaException {

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/performance/cache-mode?site_id=41472615&cache_mode=aggressive&aggressive_cache_duration=5_hr";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        CacheMode cacheMode = CacheMode.builder().cacheMode(CacheModes.aggressive).aggressiveCacheDuration(5)
                .durationPeriod(DurationPeriod.hr)
                .build();

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = cacheService.configureCacheMode("/api/prov/v1/sites/performance/cache-mode", siteId,
                cacheMode);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void configureCacheRulesTest() throws IncapsulaException {
        String siteId = "41472615";
        String path = "/api/prov/v1/sites/performance/caching-rules?site_id=41472615"
                + "&cache_headers=Content-Type,Accept"
                + "&always_cache_resource_url=https://test.domain.com,https://test2.domain.com"
                + "&always_cache_resource_pattern=contains,equals"
                + "&always_cache_resource_duration=5_hr"
                + "&never_cache_resource_url=https://my.domain.com,https://my2.domain.com"
                + "&never_cache_resource_pattern=contains,equals"
                + "&clear_always_cache_rules=false"
                + "&clear_never_cache_rules=false"
                + "&clear_cache_headers_rules=false";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        CacheRules cacheRules = CacheRules.builder()
                .cacheHeaders(Arrays.asList("Content-Type", "Accept"))
                .alwaysCacheResourceUrl(Arrays.asList("https://test.domain.com", "https://test2.domain.com"))
                .alwaysCacheResourcePattern(Arrays.asList(UrlPatterns.contains, UrlPatterns.equals))
                .alwaysCacheResourceDuration(5)
                .durationPeriod(DurationPeriod.hr)
                .neverCacheResourceUrl(Arrays.asList("https://my.domain.com", "https://my2.domain.com"))
                .neverCacheResourcePattern(Arrays.asList(UrlPatterns.contains, UrlPatterns.equals))
                .clearAlwaysCacheRules(false)
                .clearNeverCacheRules(false)
                .clearCacheHeadersRules(false)
                .build();

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = cacheService.configureCacheRules("/api/prov/v1/sites/performance/caching-rules",
                siteId, cacheRules);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void configureCacheSettingsTest() throws IncapsulaException {
        String siteId = "41472615";
        String path = "/api/prov/v1/sites/performance/advanced?site_id=41472615&param=cache_300x&value=true";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        CacheSettings cacheSettings = CacheSettings.builder().param(CacheSettingParams.cache_300x)
                .value(true)
                .build();

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = cacheService.configureCacheSettings("/api/prov/v1/sites/performance/advanced",
                siteId, cacheSettings);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");

    }

}
