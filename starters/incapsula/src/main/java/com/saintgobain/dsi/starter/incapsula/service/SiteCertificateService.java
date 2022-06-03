package com.saintgobain.dsi.starter.incapsula.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.autoconfigure.SaintGobainApiIncapsulaProperties;
import com.saintgobain.dsi.starter.incapsula.bean.SiteCertificate;
import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class SiteCertificateService extends IncapsulaService {

    private final SaintGobainApiIncapsulaProperties properties;

    public SiteCertificateService(RestTemplate incapsulaRestTemplate, SaintGobainApiIncapsulaProperties properties) {
        super(incapsulaRestTemplate);
        this.properties = properties;
    }

    public IncapsulaResponse uploadCertificate(String endpoint, String siteId, @Valid SiteCertificate certificate)
            throws IncapsulaException {

        String certificateValue = certificate.getCertificate();

        if (!certificateValue.contains("-----BEGIN CERTIFICATE-----") || !certificateValue.contains(
                "-----END CERTIFICATE-----")) {
            throw new BadRequestException("Certificate is not in Base64 Format");
        }

        String privateKey = certificate.getPrivatekey();
        String passphrase = certificate.getPassphrase();

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("api_id", properties.getApiId());
        data.add("api_key", properties.getApiKey());
        data.add("site_id", siteId);
        data.add("certificate", certificateValue);

        if (privateKey != null) {

            if (!privateKey.contains("-----BEGIN RSA PRIVATE KEY-----") || !privateKey.contains(
                    "-----END RSA PRIVATE KEY-----")) {
                throw new BadRequestException("Private key is not in Base64 Format");
            }

            data.add("private_key", privateKey);
        }

        if (passphrase != null) {
            data.add("passphrase", passphrase);
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder, data);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse removeCertificate(String endpoint, String siteId)
            throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }
}
