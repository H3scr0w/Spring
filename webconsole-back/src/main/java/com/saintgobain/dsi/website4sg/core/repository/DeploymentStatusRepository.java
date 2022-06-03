package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentStatusEntity;

public interface DeploymentStatusRepository extends JpaRepository<DeploymentStatusEntity, String> {

}
