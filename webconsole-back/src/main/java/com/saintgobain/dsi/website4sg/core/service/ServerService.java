package com.saintgobain.dsi.website4sg.core.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.ServerEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.ServerRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.ServerSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Server;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerDetailBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.ServerMapper;

/**
 * The Class ServerService.
 */
@Service
@Transactional
public class ServerService {

    /** The server repository. */
    private final ServerRepository serverRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    /**
     * Instantiates a new server service.
     *
     * @param serverRepository the server repository
     * @param projectAccessControl the project access control
     * @param admin the admin
     */
    public ServerService(ServerRepository serverRepository, ProjectAccessControl projectAccessControl, Admin admin) {
        this.serverRepository = serverRepository;
        this.projectAccessControl = projectAccessControl;
        this.admin = admin;
    }

    /**
     * Gets the all servers.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all servers
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<ServerDetailBody> getAllServers(Pageable pageable, String name, Authentication authentication,
            String search)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<ServerEntity> spec = Specification.where(null);
        Page<ServerEntity> serverPages = new PageImpl<ServerEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(null, null, name, search, null);
        } else {
            spec = generateSpecification(Arrays.asList(Roles.Admin.name()), authentication.getName(), name, search,
                    null);
        }
        serverPages = serverRepository.findAll(spec, pageable);

        return ServerMapper.toServerBodyPage(serverPages.getContent(), pageable, serverPages.getTotalElements());
    }

    /**
     * Gets the server.
     *
     * @param hostname the hostname
     * @param authentication the authentication
     * @return the server
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public ServerDetailBody getServer(String hostname, Authentication authentication) throws Website4sgCoreException {
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;
        ServerEntity serverEntity = serverRepository.findByHostname(hostname).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.SERVER_NOT_FOUND.name()));

        if (!isAdmin) {
            isAllowed = filterAccessToServers(serverEntity, authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {
            return ServerMapper.toServerDetailBody(serverEntity);
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * Creates the or update server.
     *
     * @param hostname the hostname
     * @param server the server
     * @param authentication the authentication
     * @return the server entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional
    public ServerDetailBody createOrUpdateServer(String hostname, Server server, Authentication authentication)
            throws Website4sgCoreException {
        ServerEntity serverEntity = serverRepository.findByHostname(hostname).orElse(ServerMapper.toServerEntity(server)
                .toBuilder().created(new Date()).hostname(hostname).build());
        serverEntity.setDomain(server.getDomain());
        serverEntity.setEnable(server.getEnable());
        serverEntity.setLogin(server.getLogin());
        serverEntity.setSshServer(server.getSshServer());
        serverEntity.setLastUpdate(new Date());
        return ServerMapper.toServerDetailBody(serverRepository.save(serverEntity));
    }

    public Specification<ServerEntity> generateSpecification(List<String> roles, String email, String hostname,
            String search, Long docrootEnvId) {
        Specification<ServerEntity> spec = Specification.where(null);

        if (!CollectionUtils.isEmpty(roles) && StringUtils.isNotBlank(email)) {
            Specification<ServerEntity> drupalRolesSpec = ServerSpecification.joinWithUserRoles(
                    ProjectTypeId.D_DOCROOTCORE,
                    roles, email);
            Specification<ServerEntity> websiteRolesSpec = ServerSpecification.joinWithUserRoles(
                    ProjectTypeId.D_WEBSITE,
                    roles, email);
            spec = spec.and(drupalRolesSpec.or(websiteRolesSpec));
        }

        if (StringUtils.isNotBlank(hostname)) {
            spec = spec.and(ServerSpecification.hostnameSpecification(hostname));
        }

        if (StringUtils.equalsIgnoreCase("ssh", search)) {
            spec = spec.and(ServerSpecification.sshSpecification(true));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        if (docrootEnvId != null) {
            spec = spec.and(ServerSpecification.joinWithDocrootEnv(docrootEnvId));
        }

        return spec;
    }

    /**
     * Filter access to servers.
     *
     * @param server the server
     * @param authentication the authentication
     * @param roles the roles
     * @return true, if successful
     */
    private boolean filterAccessToServers(ServerEntity server, Authentication authentication, Roles... roles) {

        List<DocrootEnvironmentEntity> serverEnvs = server.getDocrootenvironmentByServer();

        List<DocrootEnvironmentEntity> serverEnvsFiltred = serverEnvs.stream().filter(docrootEnv -> projectAccessControl
                .isAuthorized(docrootEnv
                        .getDrupaldocrootcore().getProject(), authentication, roles))
                .collect(Collectors
                        .toList());

        if (CollectionUtils.isEmpty(serverEnvsFiltred)) {

            List<WebsiteDeployedEntity> websitesDeployed = serverEnvs.stream().map(docrootEnv -> docrootEnv
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



}
