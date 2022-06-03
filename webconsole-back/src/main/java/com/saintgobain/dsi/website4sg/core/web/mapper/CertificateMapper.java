package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;

import com.saintgobain.dsi.website4sg.core.domain.referential.CertificateEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Certificate;

public class CertificateMapper {

    public static Certificate toCertificate(CertificateEntity certificateEntity) {
        if (certificateEntity == null) {
            return null;
        }

        return Certificate.builder()
                .code(certificateEntity.getCode())
                .name(certificateEntity.getName())
                .passphrase(certificateEntity.getPassphrase())
                .created(certificateEntity.getCreated())
                .lastUpdate(certificateEntity.getLastUpdate())
                .updatedBy(certificateEntity.getUpdatedBy())
                .build();
    }

    public static Page<Certificate> toCertificateList(Page<CertificateEntity> certificateEntities) {
        List<Certificate> certificateList = certificateEntities.getContent()
                .stream()
                .map(certificateEntity -> toCertificate(certificateEntity))
                .collect(Collectors.toList());

        return new PageImpl<Certificate>(certificateList, certificateEntities.getPageable(), certificateEntities
                .getTotalElements());

    }

    public static CertificateEntity toCertificateEntity(Certificate certificate, Authentication authentication) {
        if (certificate == null) {
            return null;
        }

        return CertificateEntity.builder()
                .name(certificate.getName())
                .passphrase(certificate.getPassphrase())
                .created(new Date())
                .lastUpdate(new Date())
                .updatedBy(authentication.getName())
                .build();
    }

    public static CertificateEntity toNewCertificateEntity(CertificateEntity certificateEntity, Certificate certificate,
            Authentication authentication) {
        if (certificate == null) {
            return null;
        }

        return certificateEntity.toBuilder()
                .name(certificate.getName())
                .passphrase(certificate.getPassphrase())
                .lastUpdate(new Date())
                .updatedBy(authentication.getName())
                .build();
    }

}
