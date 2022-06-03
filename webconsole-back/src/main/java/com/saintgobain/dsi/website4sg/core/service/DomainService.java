package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
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

import com.saintgobain.dsi.starter.incapsula.bean.AclsConfiguration;
import com.saintgobain.dsi.starter.incapsula.bean.CacheMode;
import com.saintgobain.dsi.starter.incapsula.bean.CacheRules;
import com.saintgobain.dsi.starter.incapsula.bean.CacheSettings;
import com.saintgobain.dsi.starter.incapsula.bean.Site;
import com.saintgobain.dsi.starter.incapsula.bean.SiteCertificate;
import com.saintgobain.dsi.starter.incapsula.bean.SiteCertificate.SiteCertificateBuilder;
import com.saintgobain.dsi.starter.incapsula.bean.Statistic;
import com.saintgobain.dsi.starter.incapsula.bean.StatsNames;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatsConfiguration;
import com.saintgobain.dsi.starter.incapsula.bean.TimeRanges;
import com.saintgobain.dsi.starter.incapsula.bean.TrafficVisit;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;
import com.saintgobain.dsi.starter.incapsula.service.AclService;
import com.saintgobain.dsi.starter.incapsula.service.CacheService;
import com.saintgobain.dsi.starter.incapsula.service.IncapsulaResponse;
import com.saintgobain.dsi.starter.incapsula.service.SiteCertificateService;
import com.saintgobain.dsi.starter.incapsula.service.SiteService;
import com.saintgobain.dsi.starter.incapsula.service.ThreatService;
import com.saintgobain.dsi.starter.incapsula.service.TrafficService;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.DomainTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.CertificateEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainTypeEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.RegistarEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.CertificateRepository;
import com.saintgobain.dsi.website4sg.core.repository.DocrootEnvironmentRepository;
import com.saintgobain.dsi.website4sg.core.repository.DomainRepository;
import com.saintgobain.dsi.website4sg.core.repository.DomainTypeRepository;
import com.saintgobain.dsi.website4sg.core.repository.RegistarRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteDeployedRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.security.Roles;
import com.saintgobain.dsi.website4sg.core.specification.DomainSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.DomainMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;

    private final RegistarRepository registarRepository;

    private final CertificateRepository certificateRepository;

    private final WebsiteRepository websiteRepository;

    private final DocrootEnvironmentRepository docrootEnvironmentRepository;

    private final WebsiteDeployedRepository websiteDeployedRepository;

    private final DomainTypeRepository domainTypeRepository;

    private final DocrootService docrootService;

    private final EnvironmentService environmentService;

    private final SiteService siteService;

    private final ThreatService threatService;

    private final AclService aclService;

    private final SiteCertificateService siteCertificateService;

    private final CacheService cacheService;

    private final TrafficService trafficService;

    private final ProjectAccessControl projectAccessControl;

    private final Admin admin;

    @Transactional(readOnly = true)
    public Boolean isAdmin(String domainCode, Authentication authentication) {
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        DomainEntity domain = domainRepository.findByCode(domainCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.DOMAIN_NOT_FOUND.name() + " with Code: " + domainCode));

        if (!isAdmin) {
            List<WebsiteEntity> websites = websiteRepository.findAllDistinctByWebsitedeployedByWebsite_Domains_Code(
                    domain
                            .getCode());
            isAllowed = websites.stream().anyMatch(w -> projectAccessControl.isAuthorized(w.getProject(),
                    authentication, Roles.LocalIT, Roles.Admin));
        }

        return isAdmin || isAllowed;
    }

    @Transactional(readOnly = true)
    public Page<Domain> getAllDomain(Pageable pageable, String name, Boolean qualysEnabled, Boolean waf,
            Authentication authentication, String search) throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<DomainEntity> spec = Specification.where(null);
        Page<DomainEntity> domains = new PageImpl<DomainEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(name, qualysEnabled, null, null, null, waf, null, null, search);

        } else {

            spec = generateSpecification(name, qualysEnabled, authentication.getName(), null, null, waf, null, null,
                    search);
        }
        domains = domainRepository.findAll(spec, pageable);
        return DomainMapper.toDomainList(domains, isAdmin);

    }

    @Transactional(readOnly = true)
    public Page<Domain> getAllOrphanDomain(Pageable pageable) {
        Specification<DomainEntity> spec = Specification.where(null);
        spec = generateSpecification(null, null, null, null, null, null, true, "", null);
        Page<DomainEntity> domainEntities = domainRepository.findAll(spec, pageable);
        return DomainMapper.toDomainList(domainEntities, true);
    }

    @Transactional
    public ResponseEntity<Domain> createOrUpdateDomain(String code, Domain domain) throws Website4sgCoreException,
            URISyntaxException, UnsupportedEncodingException {

        RegistarEntity registarEntity = null;
        if (StringUtils.isNotBlank(domain.getRegistarCode())) {
            registarEntity = registarRepository.findByCode(domain.getRegistarCode()).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.REGISTAR_NOT_FOUND.name()));
        }

        // Website
        WebsiteEntity website = websiteRepository.findByCode(domain.getWebsiteCode()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        // Docroot
        DocrootEntity docroot = docrootService.selectByCode(domain.getDocrootCode()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.DOCROOT_NOT_FOUND.name()));

        // Env
        EnvironmentEntity env = environmentService.selectByCode(domain.getEnvironmentCode()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        // DocrootEnv
        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        // Website deployed
        WebsiteDeployedEntity websiteDeployed = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.WEBSITE_NOT_DEPLOYED.name()));

        // Check domain type & parent
        DomainTypeEntity domainType = domainTypeRepository.findByDomainTypeIdIgnoreCase(domain.getDomainType())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.DOMAIN_TYPE_NOT_FOUND.name()));

        DomainEntity domainEntity = domainRepository.findByCode(code).orElseGet(() -> DomainMapper.toDomainEntity(
                domain).toBuilder()
                .code(code)
                .build());

        boolean isNew = domainEntity.getDomainId() == null;
        domainEntity.setRegistar(registarEntity);
        domainEntity.setDomainType(domainType);
        domainEntity.setWebsiteDeployed(websiteDeployed);

        if (domain.getParent() != null) {

            // Contribution & Main cannot have parent
            if (StringUtils.equalsAnyIgnoreCase(domainType.getDomainTypeId(),
                    DomainTypeId.Contribution.name(),
                    DomainTypeId.Main.name())) {
                throw new BadRequestException(ErrorCodes.DOMAIN_MAIN_CANNOT_HAVE_PARENT.name() + " and "
                        + ErrorCodes.DOMAIN_CONTRIBUTION_CANNOT_HAVE_PARENT.name());
            }

            DomainEntity domainParentEntity = domainRepository.findByCode(domain.getParent().getCode()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.DOMAIN_NOT_FOUND.name()));

            // Contribution & Redirection cannot have child
            if (StringUtils.equalsAnyIgnoreCase(domainParentEntity.getDomainType().getDomainTypeId(),
                    DomainTypeId.Contribution.name(), DomainTypeId.Redirection.name())) {
                throw new BadRequestException("Domain: " + domainParentEntity.getCode() + " "
                        + ErrorCodes.DOMAIN_CONTRIBUTION_CANNOT_HAVE_CHILD.name() + " and "
                        + ErrorCodes.DOMAIN_REDIRECTION_CANNOT_HAVE_CHILD.name());
            }

            domainEntity.setParent(domainParentEntity);
        }

        if (isNew) {

            return ResponseEntity.created(new URI("/api/v1/hosting/domains/" + URLEncoder.encode(code,
                    StandardCharsets.UTF_8.name()))).body(
                            DomainMapper.toDomain(domainRepository.save(domainEntity), true));
        } else {
            domainEntity = DomainMapper.toNewDomainEntity(domainEntity, domain);

            return ResponseEntity.ok(DomainMapper.toDomain(domainRepository.save(domainEntity), true));
        }
    }

    @Transactional
    public void delete(String domainCode) throws Website4sgCoreException {
        DomainEntity domainEntity = domainRepository.findByCode(domainCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOMAIN_NOT_FOUND.name()));

        if (!CollectionUtils.isEmpty(domainEntity.getChildren())) {
            deleteRecursively(domainEntity.getChildren());
        }

        domainRepository.delete(domainEntity);
    }

    @Transactional
    public ResponseEntity<Domain> transferDomain(String domainCode, String websiteCode, String docrootCode,
            String environmentCode) throws Website4sgCoreException {

        DomainEntity domainEntity = domainRepository.findByCode(domainCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DOMAIN_NOT_FOUND.name()));

        if (!StringUtils.equalsAnyIgnoreCase(domainEntity.getDomainType().getDomainTypeId(),
                DomainTypeId.Contribution.name(), DomainTypeId.Main.name())) {
            throw new BadRequestException(ErrorCodes.DOMAIN_TRANSFER_CONTRIBUTION_OR_MAIN_ONLY.name());
        }

        // Website
        WebsiteEntity website = websiteRepository.findByCode(websiteCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        // Impossible to transfer domain to another site
        if (domainEntity.getWebsiteDeployed() != null && !StringUtils.equals(domainEntity.getWebsiteDeployed()
                .getWebsite().getCode(), websiteCode)) {
            throw new BadRequestException(ErrorCodes.DOMAIN_TRANSFER_OTHER_SITE_FORBIDDEN.name());
        }

        // Docroot
        DocrootEntity docroot = docrootService.selectByCode(docrootCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.DOCROOT_NOT_FOUND.name()));

        // Env
        EnvironmentEntity env = environmentService.selectByCode(environmentCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ENVIRONMENT_NOT_FOUND.name()));

        // DocrootEnv
        DocrootEnvironmentEntity docrootEnv = docrootEnvironmentRepository
                .findByDocrootIdAndEnvironmentId(docroot.getDocrootId(), env.getEnvironmentId()).orElseThrow(
                        () -> new EntityNotFoundException(ErrorCodes.DOCROOTENVIRONMENT_NOT_FOUND.name()));

        // Website deployed
        WebsiteDeployedEntity websiteDeployed = websiteDeployedRepository
                .findByWebsiteIdAndDocrootEnvironmentId(website.getWebsiteId(), docrootEnv.getDocrootEnvironmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.WEBSITE_NOT_DEPLOYED.name()));

        if (!CollectionUtils.isEmpty(domainEntity.getChildren())) {
            transferRecursively(domainEntity.getChildren(), websiteDeployed);
        }
        domainEntity.setWebsiteDeployed(websiteDeployed);

        return ResponseEntity.ok(DomainMapper.toDomain(domainRepository.save(domainEntity), true));
    }

    public IncapsulaResponse addSite(Site site) throws Website4sgCoreException, IncapsulaException {

        if (site.getDomain() == null) {
            throw new BadRequestException("Domain has to be set");
        }

        domainRepository.findByCode(site.getDomain()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.DOMAIN_NOT_FOUND.name()));

        return siteService.addSite("/api/prov/v1/sites/add", site);
    }

    public IncapsulaResponse getStatus(String domainCode, Site site, Authentication authentication)
            throws Website4sgCoreException, IncapsulaException {

        String wafId = getWafId(domainCode, authentication);
        return siteService.getStatus("/api/prov/v1/sites/status", wafId, site);

    }

    public IncapsulaResponse configureSite(String domainCode, Site site, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return siteService.configureSite("/api/prov/v1/sites/configure", wafId, site);
    }

    public IncapsulaResponse configureSecurity(String domainCode, ThreatsConfiguration conf,
            Authentication authentication) throws Website4sgCoreException,
            IncapsulaException {

        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return threatService.configureSecurity("/api/prov/v1/sites/configure/security", wafId, conf);

    }

    public IncapsulaResponse configureAcl(String domainCode, AclsConfiguration conf, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {

        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return aclService.configureAcl("/api/prov/v1/sites/configure/acl", wafId, conf);

    }

    public IncapsulaResponse uploadCertificate(String domainCode, String certificateCode, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {

        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);

        CertificateEntity certificateEntity = certificateRepository.findByCode(certificateCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.CERTIFICATE_NOT_FOUND.name()));

        SiteCertificateBuilder certificateBuilder = SiteCertificate.builder();

        String certificateValue = new String(certificateEntity.getValue(), StandardCharsets.UTF_8);

        certificateBuilder.certificate(certificateValue);

        if (certificateEntity.getKey() != null) {
            String privateKey = new String(certificateEntity.getKey(), StandardCharsets.UTF_8);
            certificateBuilder.privatekey(privateKey);
        }

        SiteCertificate certificate = certificateBuilder.passphrase(certificateEntity.getPassphrase()).build();

        return siteCertificateService.uploadCertificate("/api/prov/v1/sites/customCertificate/upload", wafId,
                certificate);

    }

    public IncapsulaResponse removeCertificate(String domainCode, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return siteCertificateService.removeCertificate("/api/prov/v1/sites/customCertificate/remove", wafId);

    }

    public IncapsulaResponse purgeCache(String domainCode, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return cacheService.purgeCache("/api/prov/v1/sites/cache/purge", wafId);

    }

    public IncapsulaResponse configureCacheMode(String domainCode, CacheMode cacheMode, Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return cacheService.configureCacheMode("/api/prov/v1/sites/performance/cache-mode", wafId,
                cacheMode);

    }

    public IncapsulaResponse configureCacheRules(String domainCode, CacheRules cacheRules,
            Authentication authentication) throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return cacheService.configureCacheRules("/api/prov/v1/sites/performance/caching-rules", wafId,
                cacheRules);

    }

    public IncapsulaResponse configureCacheSettings(String domainCode, CacheSettings cacheSettings,
            Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {
        String wafId = getWafId(domainCode, authentication, Roles.LocalIT, Roles.Admin);
        return cacheService.configureCacheSettings("/api/prov/v1/sites/performance/advanced", wafId,
                cacheSettings);

    }

    public IncapsulaResponse getStats(Pageable pageable, String domainCode, String statsId, String timeRange,
            Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {

        String wafId = getWafId(domainCode, authentication);

        StatsNames stat;
        try {
            stat = StatsNames.valueOf(statsId);
        } catch (IllegalArgumentException | NullPointerException e) {

            throw new EntityNotFoundException(
                    ErrorCodes.STATS_NOT_FOUND.name());
        }

        TimeRanges range = TimeRanges.today;

        try {
            range = TimeRanges.valueOf(timeRange);
        } catch (IllegalArgumentException | NullPointerException e) {
            range = TimeRanges.today;
        }

        Statistic statistic = Statistic.builder()
                .timeRange(range)
                .stats(Arrays.asList(stat))
                .build();

        return trafficService.getStats("/api/stats/v1", wafId, statistic, pageable);

    }

    public IncapsulaResponse getVisits(Pageable pageable, String domainCode, String timeRange,
            Authentication authentication)
            throws Website4sgCoreException,
            IncapsulaException {

        String wafId = getWafId(domainCode, authentication);

        TimeRanges range = TimeRanges.today;

        try {
            range = TimeRanges.valueOf(timeRange);
        } catch (IllegalArgumentException | NullPointerException e) {
            range = TimeRanges.today;
        }

        TrafficVisit visit = TrafficVisit.builder()
                .timeRange(range)
                .build();

        return trafficService.getVisits("/api/visits/v1", wafId, visit, pageable);

    }

    public Specification<DomainEntity> generateSpecification(String name,
            Boolean qualysEnabled, String email, Long docrootEnvId, String websiteCode, Boolean waf, Boolean orphan,
            String domainTypeId, String search) {
        Specification<DomainEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(DomainSpecification.name(name));
        }

        // Incapsula
        if (qualysEnabled != null) {
            spec = spec.and(DomainSpecification.qualysEnable(qualysEnabled));
        }

        if (StringUtils.isNotBlank(email)) {
            spec = spec.and(DomainSpecification.joinWithUser(email));
        }

        if (docrootEnvId != null) {
            spec = spec.and(DomainSpecification.joinWithDocrootEnv(docrootEnvId));
        }

        if (StringUtils.isNotBlank(websiteCode)) {
            spec = spec.and(DomainSpecification.joinWithWebsite(websiteCode));
        }

        // Incapsula
        if (BooleanUtils.isTrue(waf)) {
            spec = spec.and(DomainSpecification.wafIdNotNull());
        }

        if (BooleanUtils.isTrue(orphan)) {
            spec = spec.and(DomainSpecification.parentNull());
        }

        if (domainTypeId != null) {
            spec = spec.and(DomainSpecification.domainTypeId(domainTypeId));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;

    }

    private String getWafId(String domainCode, Authentication authentication, Roles... roles)
            throws Website4sgCoreException {
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        DomainEntity domain = domainRepository.findByCode(domainCode).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.DOMAIN_NOT_FOUND.name() + " with Code: " + domainCode));

        if (!isAdmin) {
            List<WebsiteEntity> websites = websiteRepository.findAllDistinctByWebsitedeployedByWebsite_Domains_Code(
                    domain
                            .getCode());
            isAllowed = websites.stream().anyMatch(w -> projectAccessControl.isAuthorized(w.getProject(),
                    authentication, roles));

            if (!isAllowed) {
                throw new ForbiddenException();
            }
        }

        String wafId = domain.getWafId();

        if (StringUtils.isBlank(wafId)) {
            throw new EntityNotFoundException(ErrorCodes.DOMAIN_WAFID_NOT_FOUND.name());
        }

        return wafId;
    }


    private void deleteRecursively(List<DomainEntity> children) {

        for (DomainEntity child : children) {

            if (!CollectionUtils.isEmpty(child.getChildren())) {
                deleteRecursively(child.getChildren());
            } else {
                domainRepository.delete(child);
            }

        }

    }

    private void transferRecursively(List<DomainEntity> children, WebsiteDeployedEntity websiteDeployed) {
        for (DomainEntity child : children) {

            if (!CollectionUtils.isEmpty(child.getChildren())) {
                transferRecursively(child.getChildren(), websiteDeployed);
            }

            child.setWebsiteDeployed(websiteDeployed);
            domainRepository.save(child);

        }
    }




}
