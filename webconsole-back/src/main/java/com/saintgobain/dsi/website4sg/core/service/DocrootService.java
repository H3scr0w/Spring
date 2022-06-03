package com.saintgobain.dsi.website4sg.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DocrootRepository;
import com.saintgobain.dsi.website4sg.core.repository.HostingProviderRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.DocrootSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.DocrootMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DocrootService {

    private final DocrootRepository docRootRepository;

    private final HostingProviderRepository hostingProviderRepository;

    private final Admin admin;

    private final ProjectAccessControl projectAccessControl;

    @Transactional(readOnly = true)
    public Page<DocrootHeader> getAllDocRootHeader(Pageable pageable, String name,
            Authentication authentication, String search) throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<DocrootEntity> spec = Specification.where(null);
        Page<DocrootEntity> results = new PageImpl<DocrootEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(null, null, name, null, null, null, search);
        } else {
            spec = generateSpecification(Arrays.asList(
                    Roles.Admin.name(), Roles.LocalIT.name(), Roles.External.name(), Roles.Technical_Contact.name()),
                    authentication
                            .getName(), name, null, null, null, search);
        }

        results = docRootRepository.findAll(spec, pageable);

        return DocrootMapper.toDocrootHeaderList(results, isAdmin);
    }

    @Transactional(readOnly = true)
    public DocrootHeader getDocrootDetail(String docrootCode, Authentication authentication)
            throws Website4sgCoreException {

        DocrootEntity docroot = docRootRepository.findByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = filterAccessToDocroot(docroot, authentication, Roles.Admin, Roles.LocalIT, Roles.External,
                    Roles.Technical_Contact);
        }

        if (isAdmin || isAllowed) {
            return DocrootMapper.toDocrootHeader(docroot, isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional(readOnly = true)
    public Optional<DocrootEntity> selectByCode(String code) {
        return docRootRepository.findByCode(code);
    }

    @Transactional
    public DocrootHeader create(DocrootHeader docroot, Authentication authentication)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        if (isAdmin) {
            HostingProviderEntity hostingProviderEntity = hostingProviderRepository.findByCode(docroot
                    .getHostingProviderCode()).orElseThrow(
                            () -> new EntityNotFoundException(
                                    ErrorCodes.HOSTING_PROVIDER_NOT_FOUND.name()));

            DocrootEntity docrootEntity = new DocrootEntity();

            docrootEntity.setHostingprovider(hostingProviderEntity);

            docrootEntity.setName(docroot.getName());
            docrootEntity.setRundeckJobApiUrl(docroot.getRundeckJobApiUrl());
            docrootEntity.setCode(docroot.getCode());
            docrootEntity.setProviderInternalId(docroot.getProviderInternalId());
            return DocrootMapper.toDocrootHeader(docRootRepository.save(docrootEntity), isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional
    public DocrootHeader update(DocrootEntity docroot, DocrootHeader newDocroot, Authentication authentication)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = filterAccessToDocroot(docroot, authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {

            HostingProviderEntity hostingProviderEntity = hostingProviderRepository.findByCode(newDocroot
                    .getHostingProviderCode()).orElseThrow(
                            () -> new EntityNotFoundException(
                                    ErrorCodes.HOSTING_PROVIDER_NOT_FOUND.name()));

            docroot.setHostingprovider(hostingProviderEntity);
            docroot.setName(newDocroot.getName());
            docroot.setRundeckJobApiUrl(newDocroot.getRundeckJobApiUrl());
            docroot.setProviderInternalId(newDocroot.getProviderInternalId());
            return DocrootMapper.toDocrootHeader(docRootRepository.save(docroot), isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    private Specification<DocrootEntity> generateSpecification(List<String> roles, String email, String name,
            String code, String drupalCode, Long websiteId, String search) {
        Specification<DocrootEntity> spec = Specification.where(null);

        if (!CollectionUtils.isEmpty(roles) && StringUtils.isNotBlank(email)) {
            Specification<DocrootEntity> drupalRolesSpec = DocrootSpecification.joinWithUserRoles(
                    ProjectTypeId.D_DOCROOTCORE,
                    roles, email);
            Specification<DocrootEntity> websiteRolesSpec = DocrootSpecification.joinWithUserRoles(
                    ProjectTypeId.D_WEBSITE,
                    roles, email);
            spec = spec.and(drupalRolesSpec.or(websiteRolesSpec));
        }

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(DocrootSpecification.name(name));
        }

        if (StringUtils.isNotBlank(code)) {
            spec = spec.and(DocrootSpecification.code(code));
        }

        if (StringUtils.isNotBlank(drupalCode)) {
            spec = spec.and(DocrootSpecification.joinWithDrupal(drupalCode));
        }

        if (websiteId != null) {
            spec = spec.and(DocrootSpecification.joinWithWebsite(websiteId));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

    private boolean filterAccessToDocroot(DocrootEntity docroot, Authentication authentication, Roles... roles) {

        List<DocrootEnvironmentEntity> docrootEnvs = docroot.getDocrootenvironmentByDocroot();

        // Access filter to DrupalDocRootCore project
        List<DocrootEnvironmentEntity> docrootEnvsFiltred = docrootEnvs.stream().filter(
                docrootEnv -> projectAccessControl.isAuthorized(docrootEnv
                        .getDrupaldocrootcore().getProject(), authentication, roles))
                .collect(Collectors
                        .toList());

        // If it has no rights on DDC project and so on DocRoot
        if (CollectionUtils.isEmpty(docrootEnvsFiltred)) {

            // Access filter to Website project
            List<WebsiteDeployedEntity> websitesDeployed = docrootEnvs.stream().map(docrootEnv -> docrootEnv
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
