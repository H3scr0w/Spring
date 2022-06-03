package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.CertificateEntity;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Long>,
        JpaSpecificationExecutor<CertificateEntity> {

    Optional<CertificateEntity> findByCode(String code);

}
