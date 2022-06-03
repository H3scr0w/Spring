package com.saintgobain.dsi.website4sg.core.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintgobain.dsi.website4sg.core.domain.referential.CertificateEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.CertificateRepository;
import com.saintgobain.dsi.website4sg.core.specification.CertificateSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Certificate;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.CertificateMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    private final ObjectMapper mapper;

    @Transactional(readOnly = true)
    public Page<Certificate> getAllCertificates(String name, Pageable pageable, String search)
            throws Website4sgCoreException {
        return CertificateMapper.toCertificateList(certificateRepository.findAll(generateSpecification(name, search),
                pageable));
    }

    @Transactional
    public ResponseEntity<Certificate> createOrUpdateCertificate(String code, String certParam,
            Authentication authentication, MultipartFile certFile, MultipartFile keyFile)
            throws Website4sgCoreException,
            URISyntaxException, IOException {

        Certificate certificate = mapper.readValue(certParam, Certificate.class);

        CertificateEntity certificateEntity = certificateRepository.findByCode(code).orElseGet(() -> CertificateMapper
                .toCertificateEntity(certificate, authentication).toBuilder().code(code)
                .build());

        boolean isNew = certificateEntity.getCertificateId() == null;

        if (certFile != null) {

            String certFileContent = new String(certFile.getBytes(), StandardCharsets.UTF_8);

            if (!certFileContent.contains("-----BEGIN CERTIFICATE-----") || !certFileContent.contains(
                    "-----END CERTIFICATE-----")) {
                throw new BadRequestException(ErrorCodes.CERTIFICATE_FILE_BAD_FORMAT.name());
            }

            certificateEntity.setValue(certFile.getBytes());
        }

        if (keyFile != null) {
            String keyFileContent = new String(keyFile.getBytes(), StandardCharsets.UTF_8);

            if (!keyFileContent.contains("-----BEGIN RSA PRIVATE KEY-----") || !keyFileContent.contains(
                    "-----END RSA PRIVATE KEY-----")) {
                throw new BadRequestException(ErrorCodes.CERTIFICATE_KEY_BAD_FORMAT.name());
            }
            certificateEntity.setKey(keyFile.getBytes());
        }

        if (isNew) {

            return ResponseEntity.created(new URI("/api/v1/hosting/certificates/" + URLEncoder.encode(code,
                    StandardCharsets.UTF_8.name()))).body(
                            CertificateMapper.toCertificate((certificateRepository.save(certificateEntity))));
        } else {
            certificateEntity = CertificateMapper.toNewCertificateEntity(certificateEntity, certificate,
                    authentication);

            return ResponseEntity.ok(CertificateMapper.toCertificate(certificateRepository.save(certificateEntity)));
        }
    }

    @Transactional
    public void deleteCertificate(String code) {
        CertificateEntity certificateEntity = certificateRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.CERTIFICATE_NOT_FOUND.name()));
        certificateRepository.delete(certificateEntity);
    }

    private Specification<CertificateEntity> generateSpecification(String name, String search) {
        Specification<CertificateEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(CertificateSpecification.name(name));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

}
