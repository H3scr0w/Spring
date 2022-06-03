package com.saintgobain.dsi.website4sg.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.DomainTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DocrootRepository;
import com.saintgobain.dsi.website4sg.core.repository.DomainRepository;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.DocrootSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.WebsiteSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootBody;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.bean.Website;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteBody;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.DocrootMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.DomainMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.WebsiteMapper;

import lombok.RequiredArgsConstructor;

/**
 * The Class WebsiteService.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WebsiteService {

    /** The website repository. */
    private final WebsiteRepository websiteRepository;

    /** The project repository. */
    private final ProjectRepository projectRepository;

    /** The docroot repository. */
    private final DocrootRepository docrootRepository;

    /** The domain repository. */
    private final DomainRepository domainRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    private final DomainService domainService;

    /**
     * Select by code.
     *
     * @param code the code
     * @return the optional
     */
    @Transactional(readOnly = true)
    public Optional<WebsiteEntity> selectByCode(String code) {
        return websiteRepository.findByCode(code);
    }

    /**
     * Gets the all websites.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all websites
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<WebsiteDetailBody> getAllWebsites(Pageable pageable, String name, Boolean qualysEnabled,
            Authentication authentication, String search, Boolean showEnable)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<WebsiteEntity> spec = Specification.where(null);
        Page<WebsiteEntity> websitesList = new PageImpl<WebsiteEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(name, qualysEnabled, null, search, showEnable);

        } else {

            spec = generateSpecification(name, qualysEnabled, authentication.getName(), search, showEnable);
        }

        websitesList = websiteRepository.findAll(spec, pageable);
        return WebsiteMapper.toWebsiteDetailBodyPage(websitesList, isAdmin);

    }

    /**
     * Gets the website.
     *
     * @param websiteCode the website code
     * @param authentication the authentication
     * @return the website
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public WebsiteDetailBody getWebsite(String websiteCode, Authentication authentication)
            throws Website4sgCoreException {

        WebsiteEntity website = selectByCode(websiteCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;
        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(website.getProject(), authentication);
        }

        if (isAdmin || isAllowed) {
            return WebsiteMapper.toWebsiteDetailBody(website, isAdmin);
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * Creates the or update website.
     *
     * @param websiteCode the website code
     * @param website the website
     * @param authentication the authentication
     * @return the website body
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional
    public WebsiteBody createOrUpdateWebsite(String websiteCode, Website website, Authentication authentication)
            throws Website4sgCoreException {
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        WebsiteEntity websiteEntity = selectByCode(websiteCode).orElseGet(() -> {
            if (isAdmin) {
                WebsiteEntity websiteFound = WebsiteMapper.toWebsiteEntity(website, websiteCode);
                websiteFound = websiteRepository.save(websiteFound);
                projectRepository.save(ProjectEntity.builder().website(websiteFound).build());
                return websiteFound;
            } else {
                return null;
            }
        });

        if (websiteEntity == null) {
            throw new ForbiddenException();
        }

        websiteEntity = WebsiteMapper.toNewWebsiteEntity(websiteEntity, website);

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(websiteEntity.getProject(), authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {
            websiteEntity = websiteRepository.save(websiteEntity);
        } else {
            throw new ForbiddenException();
        }

        return WebsiteMapper.toWebsiteBody(websiteEntity, isAdmin);

    }

    /**
     * Gets the website docroots.
     *
     * @param websiteCode the website code
     * @param authentication the authentication
     * @return the website docroots
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public List<DocrootHeader> getWebsiteDocroots(String websiteCode,
            Authentication authentication)
            throws Website4sgCoreException {
        WebsiteEntity websiteFound = websiteRepository.findByCode(websiteCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = projectAccessControl.isAuthorized(websiteFound.getProject(), authentication);
        if (isAdmin || isAllowed) {

            if (!CollectionUtils.isEmpty(websiteFound.getWebsitedeployedByWebsite())) {

                List<DocrootEntity> docroots = docrootRepository.findAll(DocrootSpecification.joinWithWebsite(
                        websiteFound.getWebsiteId()));
                return DocrootMapper.toDocrootHeaderList(docroots, isAdmin);
            } else {
                throw new EntityNotFoundException(ErrorCodes.DOCROOT_NOT_FOUND.name());
            }

        } else {
            throw new ForbiddenException();
        }
    }

    @Transactional(readOnly = true)
    public List<Domain> getWebsiteDomains(String websiteCode, Boolean waf, Boolean tree, String docrootCode,
            String environmentCode, Authentication authentication)
            throws Website4sgCoreException {

        WebsiteEntity websiteFound = websiteRepository.findByCode(websiteCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = projectAccessControl.isAuthorized(websiteFound.getProject(), authentication);

        if (isAdmin || isAllowed) {

            List<DomainEntity> domainEntities = new ArrayList<DomainEntity>();
            Specification<DomainEntity> spec = Specification.where(null);

            if (isAdmin) {

                spec = domainService.generateSpecification(null, null, null, null, websiteCode, waf, null, null, null);
                
            } else {

                spec = domainService.generateSpecification(null, null, authentication.getName(), null, websiteCode, waf, null, null, null);
            }
            
            domainEntities = domainRepository.findAll(spec);

            // get only contribution domains & main domains with their redirection children for avoiding doublons for
            // domain tree and for specific environment
            if (BooleanUtils.isTrue(tree)) {

                if (StringUtils.isAnyBlank(docrootCode, environmentCode)) {
                    throw new BadRequestException(
                            "docrootCode & environmentCode are mandatory with tree query parameter");
                }

                domainEntities = domainEntities.stream().filter(domain -> !StringUtils.equalsIgnoreCase(domain
                        .getDomainType().getDomainTypeId(), DomainTypeId.Redirection.name()) && StringUtils.equals(
                                domain.getWebsiteDeployed().getDocrootenvironmentByWebsiteDeployed().getDocroot()
                                        .getCode(), docrootCode) && StringUtils.equalsIgnoreCase(domain
                                                .getWebsiteDeployed().getDocrootenvironmentByWebsiteDeployed()
                                                .getEnvironment().getCode().toLowerCase(), environmentCode)).collect(
                                                        Collectors
                                                                .toList());
            }

            return DomainMapper.toDomainList(domainEntities, isAdmin);

        } else {
            throw new ForbiddenException();
        }
    }

    @Transactional(readOnly = true)
    public List<DocrootBody> getWebsiteDocrootenvironments(String websiteCode, Authentication authentication)
            throws Website4sgCoreException {

        WebsiteEntity websiteFound = websiteRepository.findByCode(websiteCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = projectAccessControl.isAuthorized(websiteFound.getProject(), authentication);
        if (isAdmin || isAllowed) {

            if (!CollectionUtils.isEmpty(websiteFound.getWebsitedeployedByWebsite())) {

                Map<DocrootEntity, List<WebsiteDeployedEntity>> websiteDeployedByDocroot = websiteFound
                        .getWebsitedeployedByWebsite()
                        .stream()
                        .filter(websiteDeployed -> websiteDeployed.getDocrootenvironmentByWebsiteDeployed() != null
                                && websiteDeployed
                                        .getDocrootenvironmentByWebsiteDeployed()
                                        .getDocroot() != null)
                        .collect(Collectors.groupingBy(websiteDeployed -> websiteDeployed
                                .getDocrootenvironmentByWebsiteDeployed()
                                .getDocroot()));
                List<DocrootBody> results = new ArrayList<>();
                websiteDeployedByDocroot.entrySet().stream().map(entry -> DocrootMapper.toDocrootBody(entry.getKey(),
                        entry
                                .getValue(), isAdmin)).forEach(results::add);

                return results;
            } else {
                throw new EntityNotFoundException(ErrorCodes.DOCROOT_NOT_FOUND.name());
            }

        } else {
            throw new ForbiddenException();
        }
    }

    private Specification<WebsiteEntity> generateSpecification(String name,
            Boolean qualysEnabled, String email, String search, Boolean showEnable) {
        Specification<WebsiteEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(WebsiteSpecification.name(name));
        }

        if (qualysEnabled != null) {
            spec = spec.and(WebsiteSpecification.qualysEnable(qualysEnabled));
        }

        if (StringUtils.isNotBlank(email)) {
            spec = spec.and(WebsiteSpecification.joinWithUser(email));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        if (showEnable != null) {
            spec = spec.and(WebsiteSpecification.enable(showEnable));
        }

        return spec;

    }

}
