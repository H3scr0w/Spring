package com.saintgobain.dsi.website4sg.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.EnvironmentRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.EnvironmentSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Environment;
import com.saintgobain.dsi.website4sg.core.web.bean.EnvironmentBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.EnvironmentMapper;

/**
 * The Class EnvironmentService.
 */
@Service
@Transactional
public class EnvironmentService {

    /** The environment repository. */
    private final EnvironmentRepository environmentRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    /**
     * Instantiates a new environment service.
     *
     * @param environmentRepository the environment repository
     * @param projectAccessControl the project access control
     * @param admin the admin
     */
    public EnvironmentService(EnvironmentRepository environmentRepository, ProjectAccessControl projectAccessControl,
            Admin admin) {
        this.environmentRepository = environmentRepository;
        this.projectAccessControl = projectAccessControl;
        this.admin = admin;
    }

    /**
     * Gets the environment.
     *
     * @param environmentCode the environment code
     * @param authentication the authentication
     * @return the environment
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public EnvironmentBody getEnvironment(String environmentCode, Authentication authentication)
            throws Website4sgCoreException {

        EnvironmentEntity environmentEntity = selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = filterAccessToProject(environmentEntity, authentication, Roles.Admin);
        }
        if (isAdmin || isAllowed) {
            return EnvironmentMapper.toEnvironmentBody(environmentEntity);
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * Gets the all environments.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all environments
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<EnvironmentEntity> getAllEnvironments(Pageable pageable, String name,
            Authentication authentication, String search) throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<EnvironmentEntity> spec = Specification.where(null);

        if (isAdmin) {
            spec = generateSpecification(null, null, name, search);
        } else {
            spec = generateSpecification(Arrays.asList(Roles.Admin.name()), authentication.getName(), name, search);
        }
        return environmentRepository.findAll(spec, pageable);

    }

    /**
     * Creates the or update environment.
     *
     * @param environmentCode the environment code
     * @param environment the environment
     * @param authentication the authentication
     * @return the environment entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional
    public EnvironmentEntity createOrUpdateEnvironment(String environmentCode, Environment environment,
            Authentication authentication)
            throws Website4sgCoreException {
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;
        EnvironmentEntity environmentFound = null;
        Optional<EnvironmentEntity> environmentEntity = selectByCode(environmentCode);
        if (!environmentEntity.isPresent()) {
            if (isAdmin) {
                environmentFound = environmentRepository.save(EnvironmentMapper.toEnvironmentEntity(environment,
                        environmentCode));
            } else {
                throw new ForbiddenException();
            }
        } else {
            environmentFound = environmentEntity.get();

            if (!isAdmin) {
                isAllowed = filterAccessToProject(environmentFound, authentication, Roles.Admin);
            }

            if (isAdmin || isAllowed) {
                environmentFound.setCode(environmentCode.toLowerCase());
                environmentFound.setName(environment.getName());
                environmentFound = environmentRepository.save(environmentFound);
            }
        }
        return environmentFound;
    }

    /**
     * Select by code.
     *
     * @param environmentCode the environment code
     * @return the optional
     * @throws BadRequestException
     */
    @Transactional(readOnly = true)
    public Optional<EnvironmentEntity> selectByCode(String environmentCode) throws BadRequestException {
        if (StringUtils.isBlank(environmentCode)) {
            throw new BadRequestException("Bad environment code: " + environmentCode);
        }
        try {
            return environmentRepository.findByCodeIgnoreCase(environmentCode);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Bad environment code: " + environmentCode);
        }

    }

    /**
     * Filter access to project.
     *
     * @param environment the environment
     * @param authentication the authentication
     * @param roles the roles
     * @return true, if successful
     */
    private boolean filterAccessToProject(EnvironmentEntity environment, Authentication authentication,
            Roles... roles) {

        List<DocrootEnvironmentEntity> environmentsList = environment.getDocrootenvironmentByEnvironment();

        List<DocrootEnvironmentEntity> serverEnvsFiltred = environmentsList.stream().filter(
                docrootEnv -> projectAccessControl
                        .isAuthorized(docrootEnv
                                .getDrupaldocrootcore().getProject(), authentication, roles))
                .collect(Collectors
                        .toList());

        if (CollectionUtils.isEmpty(serverEnvsFiltred)) {

            List<WebsiteDeployedEntity> websitesDeployed = serverEnvsFiltred.stream().map(docrootEnv -> docrootEnv
                    .getWebsitedeployedByDocrootEnvironment()).flatMap(website -> website.stream()).collect(Collectors
                            .toList());

            websitesDeployed = websitesDeployed.stream().filter(websiteDeployed -> projectAccessControl.isAuthorized(
                    websiteDeployed.getWebsite().getProject()
                            .getDrupaldocrootcore().getProject(), authentication, roles)).collect(Collectors
                                    .toList());

            if (CollectionUtils.isEmpty(websitesDeployed)) {
                return false;
            }

        }
        return true;
    }

    private Specification<EnvironmentEntity> generateSpecification(List<String> roles, String email, String name,
            String search) {
        Specification<EnvironmentEntity> spec = Specification.where(null);

        if (!CollectionUtils.isEmpty(roles) && StringUtils.isNotBlank(email)) {
            Specification<EnvironmentEntity> drupalRolesSpec = EnvironmentSpecification.joinWithUserRoles(
                    ProjectTypeId.D_DOCROOTCORE,
                    roles, email);
            Specification<EnvironmentEntity> websiteRolesSpec = EnvironmentSpecification.joinWithUserRoles(
                    ProjectTypeId.D_WEBSITE,
                    roles, email);
            spec = spec.and(drupalRolesSpec.or(websiteRolesSpec));
        }

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(EnvironmentSpecification.nameSpecification(name));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

}
