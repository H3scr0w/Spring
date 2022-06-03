package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentTypeEntity;

public interface DeploymentTypeRepository extends JpaRepository<DeploymentTypeEntity, String> {

}
