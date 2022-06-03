package com.saintgobain.dsi.starter.incapsula.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.incapsula.autoconfigure.SaintGobainApiIncapsulaProperties;
import com.saintgobain.dsi.starter.incapsula.bean.Site;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties({
        SaintGobainApiIncapsulaProperties.class })
@TestPropertySource(properties = {
        "saint.gobain.api.waf.url=http://titi",
        "saint.gobain.api.waf.apiId=test",
        "saint.gobain.api.waf.apiKey=test"
})
public class SiteServiceTest {

    @Value("${saint.gobain.api.waf.url}")
    String url;

    @Value("${saint.gobain.api.waf.apiId}")
    String apiId;

    @Value("${saint.gobain.api.waf.apiKey}")
    String apiKey;

    @Mock
    private RestTemplate incapsulaRestTemplate;

    private SiteService siteService;

    @Before
    public void setUp() throws Exception {

        siteService = new SiteService(incapsulaRestTemplate);
    }

    @Test
    public void configureSiteTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure?site_id=41472615&param=site_ip&value=44.22.22.3,44.22.22.5";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        Site site = Site.builder().siteIp(Arrays.asList("44.22.22.3", "44.22.22.5")).removeSsl(false).build();

        IncapsulaResponse result = siteService.configureSite("/api/prov/v1/sites/configure", siteId, site);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

}
