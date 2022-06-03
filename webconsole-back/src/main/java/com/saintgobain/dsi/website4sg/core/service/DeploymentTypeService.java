package com.saintgobain.dsi.website4sg.core.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentTypeEntity;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentTypeRepository;

@Service
@Transactional
public class DeploymentTypeService {

    private DeploymentTypeRepository deploymentTypeRepository;

    public DeploymentTypeService(DeploymentTypeRepository deploymentTypeRepository) {
        this.deploymentTypeRepository = deploymentTypeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DeploymentTypeEntity> find(String id) {
        return deploymentTypeRepository.findById(id);
    }

}
