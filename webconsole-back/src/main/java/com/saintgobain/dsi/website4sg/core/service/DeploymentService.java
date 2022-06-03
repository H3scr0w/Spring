package com.saintgobain.dsi.website4sg.core.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentCommandEntity;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentStatusEntity;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentTypeEntity;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.DeploymentStatusId;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.DeploymentTypeId;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.EnvironmentCode;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.RundeckStatus;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentCommandRepository;
import com.saintgobain.dsi.website4sg.core.repository.DeploymentRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.DeploymentSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;
import com.saintgobain.dsi.website4sg.core.web.bean.DeploymentStatus;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreDeployment;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalWebsiteDeployment;
import com.saintgobain.dsi.website4sg.core.web.bean.ProjectDeployment;
import com.saintgobain.dsi.website4sg.core.web.bean.Request;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.CommandMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.DeploymentMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.RequestMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;

    private final DeploymentCommandRepository deploymentCommandRepository;

    private final DeploymentStatusService deploymentStatusService;

    private final DeploymentTypeService deploymentTypeService;

    private final ProjectAccessControl projectAccessControl;

    private final Admin admin;

    private final RundeckService rundeckService;

    private final DrupalDocrootCoreService drupalDocrootCoreService;

    private final WebsiteService websiteService;

    private final EnvironmentService environmentService;

    private final DocrootEnvironmentService docrootEnvironmentService;

    private final DocrootService docrootService;

    @Transactional(readOnly = true)
    public String getLogs(Long deploymentId, Authentication authentication) throws Website4sgCoreException {

        DeploymentEntity deploymentFound = find(deploymentId).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(deploymentFound.getProject(), authentication);
        }

        if (isAdmin || isAllowed) {
            return rundeckService.getLogs(deploymentFound);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional
    public Deployment deploy(Long deploymentId, Authentication authentication) throws Website4sgCoreException {

        DeploymentEntity deployment = find(deploymentId).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {

            if (StringUtils.equalsIgnoreCase(deployment.getEnvironmentCode(), EnvironmentCode.prod.name())) {
                isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication, Roles.Admin,
                        Roles.LocalIT, Roles.Business, Roles.Owner, Roles.Application_Contact);
            }
            else {
                isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication);
            }

        }

        if (isAdmin || isAllowed) {

            ResponseEntity<String> result = rundeckService.postExecution(deployment.getRundeckJobId(),
                    String.valueOf(deploymentId), deployment.getEnvironmentCode());

            if (result == null || result.getStatusCodeValue() == HttpStatus.OK.value()) {

                DeploymentStatusEntity deploymentStatus = deploymentStatusService.find(
                        DeploymentStatusId.ACCEPTED.toString()).orElseThrow(() -> new EntityNotFoundException(
                                ErrorCodes.DEPLOYMENT_STATUS_NOT_FOUND.name()));

                DeploymentTypeId deploymentTypeId = DeploymentTypeId.valueOf(deployment.getDeploymentTypeId());

                switch (deploymentTypeId) {
                case D_DOCROOTCORE:
                    break;
                case D_WEBSITE:
                    break;
                default:
                    throw new NotImplementedException();
                }

                deployment.setDeploymentStatus(deploymentStatus);
                DeploymentEntity deploySaved = save(deployment);
                return DeploymentMapper.toDeployment(deploySaved, isAdmin);
            } else {
                throw new BadRequestException(ErrorCodes.RUNDECK_DEPLOY_KO.name());
            }

        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional
    public Deployment cancelDeploy(Long deploymentId, Authentication authentication) throws Website4sgCoreException {

        DeploymentEntity deployment = find(deploymentId).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication);
        }

        if (isAdmin || isAllowed) {
            DeploymentStatusEntity deploymentStatus = deploymentStatusService.find(
                    DeploymentStatusId.ABORTED.toString()).orElseThrow(() -> new EntityNotFoundException(
                            ErrorCodes.DEPLOYMENT_STATUS_NOT_FOUND.name()));
            deployment.setDeploymentStatus(deploymentStatus);
            DeploymentEntity deploySaved = save(deployment);
            return DeploymentMapper.toDeployment(deploySaved, isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional(readOnly = true)
    public Page<Deployment> getAlldeployments(Pageable pageable, Authentication authentication, List<String> status,
            String search)
            throws Website4sgCoreException {

        Page<DeploymentEntity> results;

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        if (isAdmin) {
            if (!CollectionUtils.isEmpty(status) && StringUtils.isEmpty(search)) {
                results = deploymentRepository.findAllDistinctByDeploymentStatusIdIn(status, pageable);
            } else if (!CollectionUtils.isEmpty(status) && !StringUtils.isEmpty(search)) {
                results = deploymentRepository.findAll(Specification.<DeploymentEntity> where(EntitySpecification
                        .searchTextInAllColumns(
                                search)).and(DeploymentSpecification.withStatus(status)), pageable);
            } else if (!StringUtils.isEmpty(search)) {
                results = deploymentRepository.findAll(Specification.where(EntitySpecification.searchTextInAllColumns(
                        search)), pageable);
            } else {
                results = deploymentRepository.findAll(pageable);
            }

        } else {

            if (!CollectionUtils.isEmpty(status) && StringUtils.isEmpty(search)) {
                results = deploymentRepository
                        .findAllDistinctByProject_AccessrightByProject_Users_EmailEqualsAndDeploymentStatusIdIn(
                                authentication.getPrincipal().toString(), status, pageable);
            } else if (!CollectionUtils.isEmpty(status) && !StringUtils.isEmpty(search)) {
                results = deploymentRepository.findAll(Specification.<DeploymentEntity> where(EntitySpecification
                        .searchTextInAllColumns(
                                search)).and(DeploymentSpecification.withStatus(status)).and(DeploymentSpecification
                                        .joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);
            }

            else if (!StringUtils.isEmpty(search)) {
                results = deploymentRepository
                        .findAll(Specification.<DeploymentEntity> where(EntitySpecification
                                .searchTextInAllColumns(
                                        search)).and(DeploymentSpecification.joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);
            } else {

                results = deploymentRepository
                        .findAllDistinctByProject_AccessrightByProject_Users_EmailEquals(
                                authentication.getPrincipal().toString(), pageable);
            }

        }

        return DeploymentMapper.toDeploymentPageableList(results, isAdmin);
    }

    @Transactional
    public Deployment updateStatus(Long deploymentId, DeploymentStatus status)
            throws Website4sgCoreException {

        DeploymentStatusId value = DeploymentStatusId.valueOf(status.getDeploymentStatusId());
        DeploymentEntity deployment = deploymentRepository.findById(deploymentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        DeploymentStatusEntity deploymentStatus = deploymentStatusService.find(value.toString()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_STATUS_NOT_FOUND.name()));
        deployment.setDeploymentStatus(deploymentStatus);
        deployment.setUpdated(new Date());

        return DeploymentMapper.toDeployment(deploymentRepository.save(deployment), true);

    }

    @Transactional
    public Deployment updateRundeckJobId(Long deploymentId, @NotNull String rundeckJobId, Authentication authentication)
            throws Website4sgCoreException {

        DeploymentEntity deployment = deploymentRepository.findById(deploymentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {
            deployment.setRundeckJobId(rundeckJobId);
            deployment.setUpdated(new Date());
            return DeploymentMapper.toDeployment(deploymentRepository.save(deployment), isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional(readOnly = true)
    public Deployment getDeploymentDetail(Long deploymentId, Authentication authentication)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        DeploymentEntity deploymentFound = null;

        if (isAdmin) {
            deploymentFound = deploymentRepository.findById(deploymentId).orElseThrow(() -> new EntityNotFoundException(
                    ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));
        } else {
            deploymentFound = deploymentRepository.findByDeploymentIdAndRequester(deploymentId, authentication
                    .getPrincipal().toString()).orElseThrow(() -> new EntityNotFoundException(
                            ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));
        }

        return DeploymentMapper.toDeployment(deploymentFound, isAdmin);
    }

    @Transactional(readOnly = true)
    public ProjectDeployment getDeployConf(Long deploymentId, Authentication authentication)
            throws Website4sgCoreException {

        DeploymentEntity deployment = find(deploymentId).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(deployment.getProject(), authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {

            DeploymentTypeId deploymentTypeId = DeploymentTypeId.valueOf(deployment.getDeploymentTypeId());

            switch (deploymentTypeId) {
            case D_DOCROOTCORE:
                return getConfDrupalDocrootCore(deployment);
            case D_WEBSITE:
                return getConfDrupalWebsite(deployment);
            default:
                throw new NotImplementedException();
            }
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional
    public Deployment requestDeploy(Request request, Authentication authentication)
            throws Website4sgCoreException {

        DeploymentTypeId deploymentTypeId = DeploymentTypeId.valueOf(request.getDeploymentTypeId());

        switch (deploymentTypeId) {
        case D_DOCROOTCORE:
            // DDC project
            return requestDeployDrupalDocrootCore(request, authentication);
        case D_WEBSITE:
            // Website project
            return requestDeployDrupalWebsite(request, authentication);
        default:
            throw new NotImplementedException();
        }
    }

    @Transactional
    public Deployment callbackRundeck(String id, String status) throws Website4sgCoreException {

        DeploymentEntity deployment = deploymentRepository.findByRundeckJobId(id).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_NOT_FOUND.name()));

        RundeckStatus rundeckStatus = RundeckStatus.valueOf(status);
        DeploymentStatusEntity deploymentStatus = null;

        switch (rundeckStatus) {
        case running:
            deploymentStatus = deploymentStatusService.find(DeploymentStatusId.IN_PROGRESS.toString()).get();
            break;
        case failed:
            deploymentStatus = deploymentStatusService.find(DeploymentStatusId.FAILED.toString()).get();
            break;
        case aborted:
            deploymentStatus = deploymentStatusService.find(DeploymentStatusId.ABORTED.toString()).get();
            break;
        case succeeded:
            deploymentStatus = deploymentStatusService.find(DeploymentStatusId.SUCCEEDED.toString()).get();
            break;
        default:
            throw new NotImplementedException();
        }
        deployment.setDeploymentStatus(deploymentStatus);
        return DeploymentMapper.toDeployment(deploymentRepository.save(deployment), true);

    }

    private Optional<DeploymentEntity> find(Long deploymentId) {
        return deploymentRepository.findById(deploymentId);
    }

    private DeploymentEntity save(DeploymentEntity deploymentEntity) {
        return deploymentRepository.save(deploymentEntity);
    }

    private DrupalDocrootCoreDeployment getConfDrupalDocrootCore(DeploymentEntity deployment) {

        DrupalDocrootCoreEntity ddc = deployment.getProject().getDrupaldocrootcore();

        DrupalDocrootCoreDeployment drupalDocrootCoreDeployment = DrupalDocrootCoreDeployment
                .builder()
                .deploymentTypeId(DeploymentTypeId.D_DOCROOTCORE.toString())
                .versionToDeploy(deployment.getDeliverableVersion())
                .binaryRepositoryUrl(ddc.getBinaryRepositoryUrl())
                .commands(CommandMapper.toCommandHeaderList(deployment.getDeploymentCommand()))
                .docrootCode(deployment.getDocrootCode())
                .rundeckJobId(deployment.getRundeckJobId())
                .environmentCode(deployment.getEnvironmentCode())
                .drupalDocrootCoreCode(ddc.getCode())
                .build();

        return drupalDocrootCoreDeployment;

    }

    private DrupalWebsiteDeployment getConfDrupalWebsite(DeploymentEntity deployment) {

        WebsiteEntity website = deployment.getProject().getWebsite();

        DrupalWebsiteDeployment drupalWebsiteDeployment = DrupalWebsiteDeployment.builder()
                .deploymentTypeId(DeploymentTypeId.D_WEBSITE.toString())
                .versionToDeploy(deployment.getDeliverableVersion())
                .binaryRepositoryUrl(website.getBinaryRepositoryUrl())
                .commands(CommandMapper.toCommandHeaderList(deployment.getDeploymentCommand()))
                .docrootCode(deployment.getDocrootCode())
                .rundeckJobId(deployment.getRundeckJobId())
                .environmentCode(deployment.getEnvironmentCode())
                .homeDirectory(website.getHomeDirectory())
                .build();

        return drupalWebsiteDeployment;
    }

    private Deployment requestDeployDrupalDocrootCore(Request request, Authentication authentication)
            throws Website4sgCoreException {

        DrupalDocrootCoreEntity ddc = drupalDocrootCoreService.selectByCode(request.getDeliverableCode()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DRUPAL_PROJECT_NOT_FOUND.name()));

        return requestDeployProject(request, authentication, ddc.getProject());
    }

    private Deployment requestDeployDrupalWebsite(Request request, Authentication authentication)
            throws Website4sgCoreException {

        WebsiteEntity website = websiteService.selectByCode(request.getDeliverableCode()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DRUPAL_PROJECT_NOT_FOUND.name()));

        return requestDeployProject(request, authentication, website.getProject());
    }

    private Deployment requestDeployProject(Request request, Authentication authentication, ProjectEntity project)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(project, authentication);
        }

        if (isAdmin || isAllowed) {
            DocrootEntity docroot = docrootService.selectByCode(request.getDocrootCode()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.DOCROOT_NOT_FOUND.name()));

            EnvironmentEntity env = environmentService.selectByCode(request.getEnvironmentCode()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

            docrootEnvironmentService.selectByDocrootAndEnvironment(
                    docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(() -> new EntityNotFoundException(
                            ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

            DeploymentTypeEntity deploymentType = deploymentTypeService.find(request.getDeploymentTypeId()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_TYPE_NOT_FOUND.name()));

            DeploymentStatusEntity deploymentStatus = deploymentStatusService.find(DeploymentStatusId.REQUESTED
                    .name()).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.DEPLOYMENT_STATUS_NOT_FOUND
                            .name()));

            DeploymentEntity newDeployment = RequestMapper.toDeploymentEntity(request);
            newDeployment.setDeploymentType(deploymentType);
            newDeployment.setDeploymentStatus(deploymentStatus);
            newDeployment.setProject(project);
            newDeployment.setRequester(authentication.getPrincipal().toString());
            newDeployment.setDocrootCode(request.getDocrootCode());
            newDeployment.setEnvironmentCode(request.getEnvironmentCode());
            newDeployment.setCreated(new Date());
            newDeployment.setRundeckJobId(docroot.getRundeckJobApiUrl());
            newDeployment = deploymentRepository.save(newDeployment);

            if (null != request.getCommands()) {

                List<DeploymentCommandEntity> newcommandVOlist = CommandMapper.toCommandVOList(request.getCommands(),
                        newDeployment);
                deploymentCommandRepository.saveAll(newcommandVOlist);
            }

            return DeploymentMapper.toDeployment(newDeployment, isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

}
