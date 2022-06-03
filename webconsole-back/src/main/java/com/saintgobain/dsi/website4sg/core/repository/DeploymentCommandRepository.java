package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentCommandEntity;

public interface DeploymentCommandRepository extends JpaRepository<DeploymentCommandEntity, Long> {

    Page<DeploymentCommandEntity> findAllByDeploymentId(Long deploymentId, Pageable pageable);

    void deleteAllByDeploymentId(Long deploymentId);
}
