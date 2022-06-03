package com.saintgobain.dsi.website4sg.core.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentCommandEntity;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentCommandRepository;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.web.bean.Command;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.CommandMapper;

@Service
@Transactional
public class DeploymentCommandService {

    private DeploymentCommandRepository deploymentCommandRepository;

    private DeploymentRepository deploymentRepository;

    private ProjectAccessControl projectAccessControl;

    private Admin admin;

    public DeploymentCommandService(DeploymentCommandRepository deploymentCommandRepository,
            DeploymentRepository deploymentRepository,
            ProjectAccessControl projectAccessControl, Admin admin) {
        this.deploymentCommandRepository = deploymentCommandRepository;
        this.deploymentRepository = deploymentRepository;
        this.admin = admin;
        this.projectAccessControl = projectAccessControl;
    }

    @Transactional(readOnly = true)
    public List<Command> getCommandsByDeploymentId(Pageable pageable,
            Long deploymentId, Authentication authentication) throws Website4sgCoreException {

        List<DeploymentCommandEntity> results = selectAll(deploymentId, pageable);

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        if (!isAdmin) {
            results = results.stream().filter(result -> projectAccessControl.isAuthorized(result.getDeployment()
                    .getProject(),
                    authentication, Roles.Admin, Roles.LocalIT,
                    Roles.External, Roles.Technical_Contact)).collect(
                            Collectors.toList());
        }

        return CommandMapper.toCommandList(results);
    }

    @Transactional
    public List<Command> createOrUpdateCommandsByDeploymentId(Long deploymentId, List<Command> newcommands,
            Authentication authentication) throws Website4sgCoreException {

        DeploymentEntity deployment = deploymentRepository.findById(deploymentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication, Roles.Admin,
                    Roles.LocalIT,
                    Roles.External, Roles.Technical_Contact);
        }

        if (isAdmin || isAllowed) {

            deploymentCommandRepository.deleteAllByDeploymentId(deploymentId);

            List<DeploymentCommandEntity> newcommandVOlist = CommandMapper.toCommandVOList(newcommands, deployment);

            return CommandMapper.toCommandList(deploymentCommandRepository.saveAll(newcommandVOlist));
        } else {
            throw new ForbiddenException();
        }

    }

    private List<DeploymentCommandEntity> selectAll(Long deploymentId, Pageable pageable) {
        Page<DeploymentCommandEntity> resultPage = deploymentCommandRepository.findAllByDeploymentId(deploymentId,
                pageable);
        return resultPage.getContent();
    }

}
