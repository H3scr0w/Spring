package com.saintgobain.dsi.starter.incapsula.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.incapsula.autoconfigure.SaintGobainApiIncapsulaProperties;
import com.saintgobain.dsi.starter.incapsula.bean.SiteCertificate;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties({
        SaintGobainApiIncapsulaProperties.class })
@TestPropertySource(properties = {
        "saint.gobain.api.waf.url=http://titi",
        "saint.gobain.api.waf.apiId=test",
        "saint.gobain.api.waf.apiKey=test"
})
public class SiteCertificateTest {

    @Mock
    private RestTemplate incapsulaRestTemplate;

    @Autowired
    public SaintGobainApiIncapsulaProperties properties;

    private SiteCertificateService siteCertificateService;

    @Before
    public void setUp() throws Exception {

        siteCertificateService = new SiteCertificateService(incapsulaRestTemplate, properties);
    }

    @Test
    public void uploadCertificateTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/customCertificate/upload?site_id=41472615";
        String certificateValue = "-----BEGIN CERTIFICATE-----\r\n" +
                "MIIGBDCCA+ygAwIBAgITGgAAAAKVyt9ryto6bQAAAAAAAjANBgkqhkiG9w0BAQsF\r\n" +
                "ADAfMR0wGwYDVQQDExRTYWludC1Hb2JhaW4gUm9vdCBDQTAeFw0xNTAzMDkxNjEx\r\n" +
                "MjlaFw0yNTAzMDkxNjIxMjlaMHUxEzARBgoJkiaJk/IsZAEZFgNuZXQxFTATBgoJ\r\n" +
                "kiaJk/IsZAEZFgVhdGNzZzESMBAGCgmSJomT8ixkARkWAmlmMRIwEAYKCZImiZPy\r\n" +
                "LGQBGRYCemExHzAdBgNVBAMTFlNhaW50LUdvYmFpbiBTZXJ2ZXIgQ0EwggIiMA0G\r\n" +
                "CSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQC8TpEC+Rfu71g2aF/d36GwH+56/3jo\r\n" +
                "r+JgME0WZ3a8mqqJaiDfonmWHb/i6gE9iz14r4NQRfAE5+RD+TO9mrnqIIk3VN22\r\n" +
                "HZdWecBk9IAdOlPAt4FgKqBvEd55rgMJFWLN7fFtyXFP6axNFcZsNslct15ctRs9\r\n" +
                "aitplY1YSh/tN75bBw1r6sAHrQiT78GS2+TNUDwbfBvi1hieFQOLDLnj6EcKubdF\r\n" +
                "8OeA/6iFOXOZt9EKlMH5sIp2T0o3w7iNgsI8BboNBTiU8r39yIc/kRIe+4433JEs\r\n" +
                "TcRW3PHDOKAcM9EiesbLvunWdVGMF0vKkcJbWx/P2A9ZplyxdcqTjKoqWQ9Vz8N2\r\n" +
                "XvihjnfJ22ib0yaIhzsi01K2tBSuN0nmF+xJOySHcYmAmKr4nrPf3t2/bMs+J8Fm\r\n" +
                "tx+KpxQSU92e6ZiiEbalthNIklrK++g068m9eO/PuGPqFLq+4NjIskdffRQFGtOa\r\n" +
                "P7uIKT8eZeq2rg/5YduiuRrGgNQeq9/e4/nEibnmC19TbtWMDuTFJk2AC7QwDjvZ\r\n" +
                "/dEU0hDxL/CpRJviF8ULVnsNT+I6zUIqeZgBzb8RVklJcaI4Q6c1yB/Cff+6fOHx\r\n" +
                "vGpFVY/JQliT0kERIVxgPA95vP8dsvbin1amS5X+gJgRX8a8Cn7bawIMKgWUQV2G\r\n" +
                "eOeDT33tJu7rywIDAQABo4HiMIHfMBAGCSsGAQQBgjcVAQQDAgEAMB0GA1UdDgQW\r\n" +
                "BBRI1wDjUzRoD2lfN6XLnadSWOb5qDAZBgkrBgEEAYI3FAIEDB4KAFMAdQBiAEMA\r\n" +
                "QTALBgNVHQ8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAfBgNVHSMEGDAWgBQwaUWY\r\n" +
                "9MO2arYMmDhdPckjbvzBXjBSBgNVHR8ESzBJMEegRaBDhkFodHRwOi8vY3JsLnNh\r\n" +
                "aW50LWdvYmFpbi5jb20vcGtpL2NybHMvU2FpbnQtR29iYWluJTIwUm9vdCUyMENB\r\n" +
                "LmNybDANBgkqhkiG9w0BAQsFAAOCAgEAuc+wa6vQXj9IRiB2uGRM6ETFdvf1GH6d\r\n" +
                "ihz6aOmuiiX8SGFpm9MHHwR3igxiYpOuAsaT4IqzfVhVc6Peqp3xY2wx+P91Jko8\r\n" +
                "FECZWz9+EVjxeKEeD/SNcHk+2i6/2d9KsoCnOmzEo29zBqpzlPt7hAj19e0dvhFY\r\n" +
                "Lmn3rNVHZxsh+pKSy5X1CZCXSljHEstQub8A4fpEXF5524dxsR7yn1bCGgNtPsjj\r\n" +
                "/IOULPseeIt281Opyy3V2HBgEWcr1NXs9eVQ0IpB+sa6HJOUwdMMcMppu8CqsiYa\r\n" +
                "vCPNb/WMHy4g4afPRCMPU4cXgZT2CIzndAr/kZ3jxAO9C+/o4BqLhE3fLAxiZ7xZ\r\n" +
                "PM+QiSpAR/0TrBpxcwD16Zo0lm2Eaqn/KAIx+hFDk3397THMA1HZqt0NvwLN48Kz\r\n" +
                "yTLtGd6rCkEOLfwgeDF4zevhQOJRmPhGtvlQZqZz+5zb6Jr3YjE1Ql2k0d7YYjwU\r\n" +
                "5H8vPhpOByqBnDPX34AJlS/UFcyp+3Tr1f5NIxqUnTOTZs2AiUr11BezvWwlENC3\r\n" +
                "noXXUk98FYqRHC6hsRZcEmFBpm0eGMmgz9FMe3Shb2gKG1mWyj/gViEnVD7jD3fY\r\n" +
                "ouRaUkrEzPf/fEYnDyZf84thWRRx7HA+1YfZU8+9of7FgJrDfcMYkz7dIVByO09u\r\n" +
                "SF4pYQXTYhs=\r\n" +
                "-----END CERTIFICATE-----";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("api_id", properties.getApiId());
        data.add("api_key", properties.getApiKey());
        data.add("site_id", siteId);
        data.add("certificate", certificateValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        SiteCertificate certificate = SiteCertificate.builder().certificate(certificateValue).build();

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(request),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = siteCertificateService.uploadCertificate(
                "/api/prov/v1/sites/customCertificate/upload", siteId, certificate);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");
    }

    @Test
    public void removeCertificateTest() throws IncapsulaException {
        // Given

        String siteId = "41472615";
        String path = "/api/prov/v1/sites/customCertificate/remove?site_id=41472615";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        IncapsulaResponse result = siteCertificateService.removeCertificate(
                "/api/prov/v1/sites/customCertificate/remove", siteId);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");

    }
}
