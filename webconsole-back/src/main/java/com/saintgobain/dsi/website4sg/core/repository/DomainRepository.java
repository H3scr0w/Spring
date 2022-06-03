package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;

public interface DomainRepository extends JpaRepository<DomainEntity, Long>, JpaSpecificationExecutor<DomainEntity> {

    Optional<DomainEntity> findByCode(String code);

    Optional<DomainEntity> findByWafId(String wafId);

    void deleteByWebsiteDeployed_WebsiteDeployedId(Long websiteDeployedId);
}
