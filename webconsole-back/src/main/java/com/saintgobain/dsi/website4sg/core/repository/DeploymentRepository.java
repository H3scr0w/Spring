package com.saintgobain.dsi.website4sg.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;

public interface DeploymentRepository extends JpaRepository<DeploymentEntity, Long>,
        JpaSpecificationExecutor<DeploymentEntity> {

    Optional<DeploymentEntity> findByDeploymentIdAndRequester(Long deploymentId, String userId);

    Optional<DeploymentEntity> findByRundeckJobId(String rundeckJobId);

    Page<DeploymentEntity> findAll(Specification<DeploymentEntity> spec, Pageable pageable);

    Page<DeploymentEntity> findAllDistinctByDeploymentStatusIdIn(List<String> status, Pageable pageable);

    Page<DeploymentEntity> findAllDistinctByProject_AccessrightByProject_Users_EmailEquals(String email,
            Pageable pageable);

    Page<DeploymentEntity> findAllDistinctByProject_AccessrightByProject_Users_EmailEqualsAndDeploymentStatusIdIn(
            String email, List<String> status,
            Pageable pageable);

}
