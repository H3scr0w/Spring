package com.saintgobain.dsi.starter.incapsula.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.incapsula.autoconfigure.SaintGobainApiIncapsulaProperties;
import com.saintgobain.dsi.starter.incapsula.bean.ActivationMode;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatActions;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatRules;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatsConfiguration;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties({
        SaintGobainApiIncapsulaProperties.class })
public class ThreatServiceTest {

    @Mock
    private RestTemplate incapsulaRestTemplate;

    private ThreatService threatService;

    @Before
    public void setUp() throws Exception {

        threatService = new ThreatService(incapsulaRestTemplate);
    }

    @Test
    public void configureSecurityBackDoorTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/security?site_id=41472615&rule_id=api.threats.backdoor&security_rule_action=api.threats.action.quarantine_url&quarantined_urls=https://test.domain.com,https://test2.domain.com";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        ThreatsConfiguration conf = ThreatsConfiguration.builder()
                .ruleId(ThreatRules.backdoor)
                .securityRuleAction(ThreatActions.quarantine_url)
                .quarantinedUrls(Arrays.asList("https://test.domain.com", "https://test2.domain.com"))
                .build();

        IncapsulaResponse result = threatService.configureSecurity("/api/prov/v1/sites/configure/security", siteId,
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
    public void configureSecurityBotAccessControlTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/security?site_id=41472615&rule_id=api.threats.bot_access_control&block_bad_bots=true&challenge_suspected_bots=true";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        ThreatsConfiguration conf = ThreatsConfiguration.builder()
                .ruleId(ThreatRules.bot_access_control)
                .blockBadBots(true)
                .challengeSuspectedBots(true)
                .build();

        IncapsulaResponse result = threatService.configureSecurity("/api/prov/v1/sites/configure/security", siteId,
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
    public void configureSecurityDdosTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/configure/security?site_id=41472615&rule_id=api.threats.ddos&activation_mode=api.threats.ddos.activation_mode.auto&ddos_traffic_threshold=10";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        ThreatsConfiguration conf = ThreatsConfiguration.builder()
                .ruleId(ThreatRules.ddos)
                .ddosTrafficThreshold(10)
                .activationMode(ActivationMode.auto)
                .build();

        IncapsulaResponse result = threatService.configureSecurity("/api/prov/v1/sites/configure/security", siteId,
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
