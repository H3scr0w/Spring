package com.saintgobain.dsi.website4sg.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;

public interface WebsiteRepository extends JpaRepository<WebsiteEntity, Long>, JpaSpecificationExecutor<WebsiteEntity> {

    Optional<WebsiteEntity> findByCode(String code);

    Optional<WebsiteEntity> findByQualysWebAppId(Long qualysWebAppId);

    /*** LocalIT Member for CUD ***/

    List<WebsiteEntity> findAllDistinctByWebsitedeployedByWebsite_Domains_Code(String domainCode);

    /*** LocalIT Member for CUD ***/

}
