package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;

public interface WebsiteDeployedRepository extends JpaRepository<WebsiteDeployedEntity, Long> {

    Optional<WebsiteDeployedEntity> findByWebsiteIdAndDocrootEnvironmentId(Long websiteId, Long docrootEnvironmentId);

    Page<WebsiteDeployedEntity> findAllByDocrootEnvironmentId(Long docrootEnvironmentId, Pageable pageable);

}
