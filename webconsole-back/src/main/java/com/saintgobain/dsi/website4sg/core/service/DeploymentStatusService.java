package com.saintgobain.dsi.website4sg.core.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentStatusEntity;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentStatusRepository;

@Service
@Transactional
public class DeploymentStatusService {

    private DeploymentStatusRepository deploymentStatusRepository;

    public DeploymentStatusService(DeploymentStatusRepository deploymentStatusRepository) {
        this.deploymentStatusRepository = deploymentStatusRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DeploymentStatusEntity> find(String id) {
        return deploymentStatusRepository.findById(id);
    }

}
