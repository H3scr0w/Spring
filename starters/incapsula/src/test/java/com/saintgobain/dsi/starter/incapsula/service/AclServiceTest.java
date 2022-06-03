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

import com.saintgobain.dsi.starter.incapsula.bean.AclsConfiguration;
import com.saintgobain.dsi.starter.incapsula.bean.AclsRules;
import com.saintgobain.dsi.starter.incapsula.bean.Continents;
import com.saintgobain.dsi.starter.incapsula.bean.Countries;
import com.saintgobain.dsi.starter.incapsula.bean.UrlPatterns;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
public class AclServiceTest {

    @Mock
    private RestTemplate incapsulaRestTemplate;

    private AclService aclService;

    @Before
    public void setUp() throws Exception {

        aclService = new AclService(incapsulaRestTemplate);
    }

    @Test
    public void configureBlacklistedUrlsTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/acl?site_id=41472615&rule_id=api.acl.blacklisted_urls&urls=https://test.domain.com,https://test2.domain.com&url_patterns=contains,equals";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        AclsConfiguration conf = AclsConfiguration.builder()
                .ruleId(AclsRules.blacklisted_urls)
                .urls(Arrays.asList("https://test.domain.com", "https://test2.domain.com"))
                .urlPatterns(Arrays.asList(UrlPatterns.contains, UrlPatterns.equals))
                .build();

        IncapsulaResponse result = aclService.configureAcl("/api/prov/v1/sites/configure/acl", siteId,
                conf);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void configureBlacklistedCountriesTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/acl?site_id=41472615&rule_id=api.acl.blacklisted_countries&countries=CA,US&continents=SA";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        AclsConfiguration conf = AclsConfiguration.builder()
                .ruleId(AclsRules.blacklisted_countries)
                .countries(Arrays.asList(Countries.CA, Countries.US))
                .continents(Arrays.asList(Continents.SA))
                .build();

        IncapsulaResponse result = aclService.configureAcl("/api/prov/v1/sites/configure/acl", siteId,
                conf);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void configureIpsTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/acl?site_id=41472615&rule_id=api.acl.blacklisted_ips&ips=";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        AclsConfiguration conf = AclsConfiguration.builder()
                .ruleId(AclsRules.blacklisted_ips)
                .build();

        IncapsulaResponse result = aclService.configureAcl("/api/prov/v1/sites/configure/acl", siteId,
                conf);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");

    }

}
