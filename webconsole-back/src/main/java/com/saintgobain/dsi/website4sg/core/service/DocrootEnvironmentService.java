package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.DomainTypeId;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainTypeEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.LoadBalancerEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.ServerEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DocrootEnvironmentRepository;
import com.saintgobain.dsi.website4sg.core.repository.DomainRepository;
import com.saintgobain.dsi.website4sg.core.repository.DomainTypeRepository;
import com.saintgobain.dsi.website4sg.core.repository.LoadBalancerRepository;
import com.saintgobain.dsi.website4sg.core.repository.ServerRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteDeployedRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.service.acquia.EnvironmentsService;
import com.saintgobain.dsi.website4sg.core.specification.DocrootEnvironmentSpecification;
import com.saintgobain.dsi.website4sg.core.specification.DomainSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentDetail;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.bean.LoadBalancer;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsitesDeployed;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsitesDeployedHeader;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.DocrootEnvironmentMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.DomainMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.LoadBalancerMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.ServerMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.WebsiteMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.WebsitesDeployedMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DocrootEnvironmentService {

    private final DocrootEnvironmentRepository docrootEnvironmentRepository;

    private final ServerRepository serverRepository;

    private final WebsiteRepository websiteRepository;

    private final WebsiteDeployedRepository websiteDeployedRepository;

    private final DomainTypeRepository domainTypeRepository;

    private final ProjectAccessControl projectAccessControl;

    private final Admin admin;

    private final DocrootService docrootService;

    private final EnvironmentService environmentService;

    private final CmsService cmsService;

    private final DrupalDocrootCoreService drupalDocrootCoreService;

    private final WebsiteService websiteService;

    private final DomainRepository domainRepository;

    private final LoadBalancerRepository loadBalancerRepository;

    private final LoadBalancerService loadBalancerService;

    private final ServerService serverService;

    private final EnvironmentsService environmentsService;

    @Transactional(readOnly = true)
    public Optional<DocrootEnvironmentEntity> selectByDocrootAndEnvironment(Long docrootId, Long environmentId) {
        return docrootEnvironmentRepository.findByDocrootIdAndEnvironmentId(docrootId, environmentId);
    }

    @Transactional
    public DocrootEnvironmentEntity save(DocrootEnvironmentEntity docrootEnv) {
        return docrootEnvironmentRepository.save(docrootEnv);
    }

    @Transactional(readOnly = true)
    public Page<DocrootEnvironmentHeader> getAllDocrootEnvironmentHeader(String docrootCode, Pageable pageable,
            Authentication authentication, String search) throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<DocrootEnvironmentEntity> spec = Specification.where(null);
        Page<DocrootEnvironmentEntity> results = new PageImpl<DocrootEnvironmentEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(null, null, docroot.getDocrootId(), search);
        } else {
            spec = generateSpecification(null, authentication.getName(), docroot.getDocrootId(), search);
        }

        results = docrootEnvironmentRepository.findAll(spec, pageable);
        return DocrootEnvironmentMapper.toDocrootEnvironmentHeaderList(results);

    }

    @Transactional(readOnly = true)
    public DocrootEnvironmentDetail getDocrootEnvironment(String docrootCode, String environmentCode,
            Authentication authentication) throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = filterAccessToDocrootEnv(docrootEnv, authentication, Roles.Admin);
        }

        if (isAdmin || isAllowed) {
            return DocrootEnvironmentMapper.toDocrootEnvironmentDetail(docrootEnv);
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional(readOnly = true)
    public Page<ServerHeader> getAllServers(Pageable pageable, String docrootCode,
            String environmentCode, String search) throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        Specification<ServerEntity> serverSpec = serverService.generateSpecification(null, null, null, search,
                docrootEnv
                        .getDocrootEnvironmentId());
        Page<ServerEntity> servers = serverRepository.findAll(serverSpec, pageable);

        return ServerMapper.toServerHeaderPage(servers);
    }

    @Transactional(readOnly = true)
    public Page<WebsitesDeployedHeader> getAllWebsitesDeployed(Pageable pageable,
            String docrootCode,
            String environmentCode) throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        Page<WebsiteDeployedEntity> websites = websiteDeployedRepository.findAllByDocrootEnvironmentId(docrootEnv
                .getDocrootEnvironmentId(), pageable);


        return WebsitesDeployedMapper.toWebsitesDeployedPage(websites);
    }

    @Transactional(readOnly = true)
    public WebsiteDetailBody getWebsiteDeployed(String docrootCode, String environmentCode, String websiteCode)
            throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        WebsiteEntity website = websiteService.selectByCode(websiteCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        WebsiteDeployedEntity websiteDeployed = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.WEBSITE_NOT_DEPLOYED.name()));

        return WebsiteMapper.toWebsiteDetailBody(websiteDeployed.getWebsite(), true);

    }

    @Transactional(readOnly = true)
    public Page<Domain> getAllDomains(Pageable pageable, String docrootCode, String environmentCode)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        Page<DomainEntity> domainEntities = domainRepository.findAll(DomainSpecification.joinWithDocrootEnv(docrootEnv
                .getDocrootEnvironmentId()), pageable);

        return DomainMapper.toDomainList(domainEntities, true);
    }

    @Transactional(readOnly = true)
    public Page<LoadBalancer> getAllLoadBalancers(Pageable pageable, String docrootCode, String environmentCode,
            String search)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        Specification<LoadBalancerEntity> loadBalancerSpec = loadBalancerService.generateSpecification(null, search,
                docrootEnv.getDocrootEnvironmentId());
        Page<LoadBalancerEntity> loadBalancerEntities = loadBalancerRepository.findAll(loadBalancerSpec, pageable);

        return LoadBalancerMapper.toLoadBalancerList(loadBalancerEntities);
    }

    @Transactional
    public ResponseEntity<DocrootEnvironmentDetail> createOrUpdate(String docrootCode, String environmentCode,
            @Valid DocrootEnvironmentDetail envDetail) throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        CmsEntity cms = cmsService.selectByCode(envDetail.getCmsCode()).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.CMS_NOT_FOUND.name()));

        DrupalDocrootCoreEntity ddc = drupalDocrootCoreService.selectByCode(envDetail
                .getDrupalDocrootCoreCode()).orElseThrow(() -> new EntityNotFoundException(
                        ErrorCodes.DRUPAL_PROJECT_NOT_FOUND.name()));

        Optional<DocrootEnvironmentEntity> docrootEnvFound = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId());

        if (docrootEnvFound.isPresent()) {

            DocrootEnvironmentEntity docrootEnv = docrootEnvFound.get();
            docrootEnv.setCms(cms);
            docrootEnv.setCmsVersion(envDetail.getCmsVersion());
            docrootEnv.setDrupaldocrootcore(ddc);
            docrootEnv.setDrupalDocrootCoreVersion(envDetail.getDrupalDocrootCoreVersion());
            docrootEnv.setCanAutoDeploy(envDetail.getCanAutoDeploy());
            docrootEnv.setProviderInternalId(envDetail.getProviderInternalId());
            docrootEnv.setAcquiaEnvironmentId(envDetail.getAcquiaEnvironmentId());
            docrootEnv = docrootEnvironmentRepository.save(docrootEnv);
            DocrootEnvironmentDetail result = DocrootEnvironmentMapper.toDocrootEnvironmentDetail(docrootEnv);
            return ResponseEntity.ok(result);

        } else {
            DocrootEnvironmentEntity newDocrootEnvironment = new DocrootEnvironmentEntity();
            newDocrootEnvironment.setCms(cms);
            newDocrootEnvironment.setDrupaldocrootcore(ddc);
            newDocrootEnvironment.setDocroot(docroot);
            newDocrootEnvironment.setEnvironment(env);
            newDocrootEnvironment.setCmsVersion(envDetail.getCmsVersion());
            newDocrootEnvironment.setDrupalDocrootCoreVersion(envDetail.getDrupalDocrootCoreVersion());
            newDocrootEnvironment.setCanAutoDeploy(envDetail.getCanAutoDeploy());
            newDocrootEnvironment.setProviderInternalId(envDetail.getProviderInternalId());
            newDocrootEnvironment.setAcquiaEnvironmentId(envDetail.getAcquiaEnvironmentId());
            newDocrootEnvironment = docrootEnvironmentRepository.save(newDocrootEnvironment);

            DocrootEnvironmentDetail result = DocrootEnvironmentMapper.toDocrootEnvironmentDetail(
                    newDocrootEnvironment);

            return ResponseEntity.created(new URI("/api/v1/hosting/docroots/" + URLEncoder.encode(newDocrootEnvironment
                    .getDocroot()
                    .getCode(), StandardCharsets.UTF_8.name()) + "/env/" + URLEncoder.encode(newDocrootEnvironment
                            .getEnvironment()
                            .getCode().toLowerCase(), StandardCharsets.UTF_8.name()))).body(
                                    result);
        }

    }

    @Transactional
    public void delete(String docrootCode, String environmentCode) throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnvFound = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        docrootEnvironmentRepository.delete(docrootEnvFound);
    }

    @Transactional
    public ServerDetailBody addServer(String docrootCode, String environmentCode, String hostname)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        ServerEntity server = serverRepository.findByHostname(hostname).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.SERVER_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (!docrootEnv.getServer().contains(server)) {
            docrootEnv.getServer().add(server);
            docrootEnvironmentRepository.save(docrootEnv);
        }

        return ServerMapper.toServerDetailBody(server);
    }

    @Transactional
    public void deleteServer(String docrootCode, String environmentCode, String hostname)
            throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        ServerEntity server = serverRepository.findByHostname(hostname).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.SERVER_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (docrootEnv.getServer().contains(server)) {
            docrootEnv.getServer().remove(server);
            docrootEnvironmentRepository.save(docrootEnv);
        }

    }

    @Transactional
    public ResponseEntity<WebsiteDetailBody> addWebsite(String docrootCode, String environmentCode, String websiteCode,
            WebsitesDeployed newWebsiteDeployed) throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        WebsiteEntity website = websiteService.selectByCode(websiteCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        Optional<WebsiteDeployedEntity> websiteDeployedFound = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId());

        if (websiteDeployedFound.isPresent()) {

            WebsiteDeployedEntity websiteDeployed = websiteDeployedFound.get();
            websiteDeployed.setWebsiteVersion(newWebsiteDeployed.getWebsiteVersion());
            return ResponseEntity.ok(WebsiteMapper.toWebsiteDetailBody(websiteDeployed.getWebsite(), true));

        } else {
            WebsiteDeployedEntity websiteDeployed = new WebsiteDeployedEntity();
            websiteDeployed.setDocrootenvironmentByWebsiteDeployed(docrootEnv);
            websiteDeployed.setWebsite(website);
            websiteDeployed.setWebsiteVersion(newWebsiteDeployed.getWebsiteVersion());
            websiteDeployed = websiteDeployedRepository.save(websiteDeployed);

            return ResponseEntity.created(new URI("/api/v1/hosting/docroots/" + URLEncoder.encode(docrootEnv
                    .getDocroot()
                    .getCode(), StandardCharsets.UTF_8.name()) + "/env/" + URLEncoder.encode(docrootEnv.getEnvironment()
                            .getCode().toLowerCase(), StandardCharsets.UTF_8.name()) + "/websites/" + URLEncoder.encode(
                                    websiteDeployed.getWebsite().getCode(), StandardCharsets.UTF_8.name()))).body(
                                            WebsiteMapper.toWebsiteDetailBody(websiteDeployed.getWebsite(), true));
        }
    }

    @Transactional
    public void deleteWebsite(String docrootCode, String environmentCode, String websiteCode)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        WebsiteEntity website = websiteService.selectByCode(websiteCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        WebsiteDeployedEntity websiteDeployed = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.WEBSITE_NOT_DEPLOYED.name()));

        domainRepository.deleteByWebsiteDeployed_WebsiteDeployedId(websiteDeployed.getWebsiteDeployedId());

        websiteDeployedRepository.delete(websiteDeployed);

    }

    @Transactional
    public Domain addDomain(String docrootCode, String environmentCode, String domainCode)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DomainEntity domain = domainRepository.findByCode(domainCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOMAIN_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (!docrootEnv.getDomain().contains(domain)) {
            docrootEnv.getDomain().add(domain);
            docrootEnvironmentRepository.save(docrootEnv);
        }

        return DomainMapper.toDomain(domain, true);
    }

    @Transactional
    public void deleteDomain(String docrootCode, String environmentCode, String domainCode)
            throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        DomainEntity domain = domainRepository.findByCode(domainCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOMAIN_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (docrootEnv.getDomain().contains(domain)) {
            docrootEnv.getDomain().remove(domain);
            docrootEnvironmentRepository.save(docrootEnv);
        }

    }

    @Transactional
    public LoadBalancer addLoadBalancer(String docrootCode, String environmentCode, String loadBalancerCode)
            throws Website4sgCoreException {

        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        LoadBalancerEntity loadBalancer = loadBalancerRepository.findByCode(loadBalancerCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.LOADBALANCER_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (!docrootEnv.getLoadbalancerByDocrootEnvironment().contains(loadBalancer)) {
            docrootEnv.getLoadbalancerByDocrootEnvironment().add(loadBalancer);
            docrootEnvironmentRepository.save(docrootEnv);
        }

        return LoadBalancerMapper.toLoadBalancer(loadBalancer);
    }

    @Transactional
    public void deleteLoadBalancer(String docrootCode, String environmentCode, String loadBalancerCode)
            throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        LoadBalancerEntity loadBalancer = loadBalancerRepository.findByCode(loadBalancerCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.LOADBALANCER_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        if (docrootEnv.getLoadbalancerByDocrootEnvironment().contains(loadBalancer)) {
            docrootEnv.getLoadbalancerByDocrootEnvironment().remove(loadBalancer);
            docrootEnvironmentRepository.save(docrootEnv);
        }

    }

    @Transactional
    public Domain attachRedirection(String docrootCode, String environmentCode, String websiteCode,
            String parentDomainCode, String childDomainCode) throws Website4sgCoreException {
        // Docroot
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        // Env
        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        // DocrootEnv
        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        // Website
        WebsiteEntity website = websiteRepository.findByCode(websiteCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        // Website deployed
        WebsiteDeployedEntity websiteDeployed = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.WEBSITE_NOT_DEPLOYED.name()));

        // Parent domain
        DomainEntity domainParentEntity = domainRepository.findByCode(parentDomainCode).orElseThrow(
                () -> new EntityNotFoundException(
                        "Parent " + ErrorCodes.DOMAIN_NOT_FOUND.name()));

        if (domainParentEntity.getWebsiteDeployed() == null || domainParentEntity.getWebsiteDeployed()
                .getWebsite() == null) {
            throw new BadRequestException("Parent Domain : " + ErrorCodes.WEBSITE_NOT_DEPLOYED.name());
        }

        if (domainParentEntity.getWebsiteDeployed().getWebsiteId() != websiteDeployed.getWebsiteId()) {
            throw new BadRequestException("Parent Domain website : " + domainParentEntity.getWebsiteDeployed()
                    .getWebsite().getCode() + " "
                    + ErrorCodes.WEBSITE_DOMAIN_NOT_SAME.name());
        }

        // Child domain
        DomainEntity domainChildEntity = domainRepository.findByCode(childDomainCode).orElseThrow(
                () -> new EntityNotFoundException(
                        "Child " + ErrorCodes.DOMAIN_NOT_FOUND.name()));

        // Contribution & Redirection cannot have child
        if (StringUtils.equalsAnyIgnoreCase(domainParentEntity.getDomainType().getDomainTypeId(),
                DomainTypeId.Contribution.name(), DomainTypeId.Redirection.name())) {
            throw new BadRequestException("Parent Domain: " + domainParentEntity.getCode() + " "
                    + ErrorCodes.DOMAIN_CONTRIBUTION_CANNOT_HAVE_CHILD.name() + " and "
                    + ErrorCodes.DOMAIN_REDIRECTION_CANNOT_HAVE_CHILD.name());
        }
        
     // Contribution & Main cannot have parent
        if (StringUtils.equalsAnyIgnoreCase(domainChildEntity.getDomainType().getDomainTypeId(),
                DomainTypeId.Contribution.name(),
                DomainTypeId.Main.name())) {
            throw new BadRequestException("Child Domain: " + domainChildEntity.getCode() + " "
                    + ErrorCodes.DOMAIN_MAIN_CANNOT_HAVE_PARENT.name() + " and "
                    + ErrorCodes.DOMAIN_CONTRIBUTION_CANNOT_HAVE_PARENT.name());
        }

        DomainTypeEntity domainType = domainTypeRepository.findByDomainTypeIdIgnoreCase(DomainTypeId.Redirection
                .toString())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.DOMAIN_TYPE_NOT_FOUND.name()));

        domainChildEntity.setParent(domainParentEntity);
        domainChildEntity.setWebsiteDeployed(websiteDeployed);
        domainChildEntity.setDomainType(domainType);
        return DomainMapper.toDomain(domainRepository.save(domainChildEntity), true);

    }

    @Transactional
    public void detachRedirection(String docrootCode, String environmentCode, String websiteCode,
            String parentDomainCode, String childDomainCode) throws Website4sgCoreException {
     // Child domain
        DomainEntity domainChildEntity = domainRepository.findByCode(childDomainCode).orElseThrow(
                () -> new EntityNotFoundException(
                        "Child " + ErrorCodes.DOMAIN_NOT_FOUND.name()));

        DomainTypeEntity domainType = domainTypeRepository.findByDomainTypeIdIgnoreCase(DomainTypeId.Contribution
                .toString())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.DOMAIN_TYPE_NOT_FOUND.name()));
        domainChildEntity.setParent(null);
        domainChildEntity.setDomainType(domainType);
        domainRepository.save(domainChildEntity);
    }

    @Transactional(readOnly = true)
    public void clearAcquiaVarnish(Authentication authentication, String docrootCode, String environmentCode,
            String domainCode)
            throws Website4sgCoreException {
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOCROOT_NOT_FOUND.name()));

        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        DomainEntity domain = domainRepository.findByCode(domainCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.DOMAIN_NOT_FOUND.name()));

        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = filterAccessToDocrootEnv(docrootEnv, authentication);
        }

        if (isAdmin || isAllowed) {
            HostingProviderEntity hostingProvider = docroot.getHostingprovider();
            if (hostingProvider == null || !StringUtils.containsIgnoreCase(hostingProvider.getCode(), "acquia")) {
                throw new BadRequestException(ErrorCodes.HOSTING_PROVIDER_NOT_ACQUIA.name());
            }

            if (StringUtils.isBlank(docrootEnv.getAcquiaEnvironmentId())) {
                throw new BadRequestException(ErrorCodes.ACQUIA_ENVIRONMENT_NOT_FOUND.name());
            }

            environmentsService.clearVarnishDomain(docrootEnv.getAcquiaEnvironmentId(), domain.getCode());
        } else {
            throw new ForbiddenException();
        }

    }

    private Specification<DocrootEnvironmentEntity> generateSpecification(List<String> roles, String email,
            Long docrootId, String search) {
        Specification<DocrootEnvironmentEntity> spec = Specification.where(null);

        if (!CollectionUtils.isEmpty(roles)) {
            Specification<DocrootEnvironmentEntity> drupalRolesSpec = DocrootEnvironmentSpecification.joinWithUserRoles(
                    ProjectTypeId.D_DOCROOTCORE,
                    roles);
            Specification<DocrootEnvironmentEntity> websiteRolesSpec = DocrootEnvironmentSpecification
                    .joinWithUserRoles(
                            ProjectTypeId.D_WEBSITE,
                            roles);
            spec = spec.and(drupalRolesSpec.or(websiteRolesSpec));
        }

        if (StringUtils.isNotBlank(email)) {
            Specification<DocrootEnvironmentEntity> drupalRolesSpec = DocrootEnvironmentSpecification.joinWithUser(
                    ProjectTypeId.D_DOCROOTCORE, email);
            Specification<DocrootEnvironmentEntity> websiteRolesSpec = DocrootEnvironmentSpecification
                    .joinWithUser(
                            ProjectTypeId.D_WEBSITE, email);
            spec = spec.and(drupalRolesSpec.or(websiteRolesSpec));
        }

        if (docrootId != null) {
            spec = spec.and(DocrootEnvironmentSpecification.docrootId(docrootId));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

    private boolean filterAccessToDocrootEnv(DocrootEnvironmentEntity docrootEnv, Authentication authentication,
            Roles... roles) {

        // Access filter to DrupalDocRootCore project
        boolean access = projectAccessControl.isAuthorized(
                docrootEnv.getDrupaldocrootcore().getProject(), authentication, roles);

        if (!access) {
            // Access filter to Website project
            List<WebsiteDeployedEntity> websitesDeployed = docrootEnv.getWebsitedeployedByDocrootEnvironment();

            websitesDeployed = websitesDeployed.stream().filter(websiteDeployed -> projectAccessControl.isAuthorized(
                    websiteDeployed.getWebsite().getProject(), authentication, roles)).collect(Collectors
                            .toList());

            if (CollectionUtils.isEmpty(websitesDeployed)) {
                access = false;
            }
        }

        return access;
    }

}
