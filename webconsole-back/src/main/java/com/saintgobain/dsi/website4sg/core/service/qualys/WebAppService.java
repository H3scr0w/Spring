package com.saintgobain.dsi.website4sg.core.service.qualys;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.bean.webapp.WebApp;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Criteria;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.service.RestService;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DomainRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteRepository;
import com.saintgobain.dsi.website4sg.core.specification.WebsiteSpecification;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class WebAppService extends RestService<WebApp> {

    private final WebsiteRepository websiteRepository;
    private final DomainRepository domainRepository;

    public WebAppService(RestTemplate qualysRestTemplate, WebsiteRepository websiteRepository,
            DomainRepository domainRepository) {
        super(qualysRestTemplate);
        this.websiteRepository = websiteRepository;
        this.domainRepository = domainRepository;
    }

    public WebApp create(WebApp webApp) throws QualysException, Website4sgCoreException {
        WebsiteEntity website = websiteRepository.findByCode(webApp.getName()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        if (BooleanUtils.isTrue(website.getIsQualysEnable())) {

            DomainEntity domain = domainRepository.findByCode(webApp.getUrl()).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.DOMAIN_NOT_FOUND.name()));

            if (BooleanUtils.isNotTrue(domain.getIsQualysEnable())) {
                throw new BadRequestException(ErrorCodes.DOMAIN_QUALYS_NOT_ENABLED.name());
            }

            StringBuilder url = new StringBuilder();

            if (BooleanUtils.isTrue(domain.getHttpsEnable())) {
                url.append("https://");
            } else {
                url.append("http://");
            }

            url.append(domain.getCode());

            webApp.setUrl(url.toString());
            OptionProfile profile = webApp.getDefaultProfile();
            profile.setOwner(null);

            WebApp qualysWebApp = create(webApp, "/create/was/webapp");
            website.setQualysWebAppId(qualysWebApp.getId());
            website.setLastUpdate(new Date());
            websiteRepository.save(website);
            return qualysWebApp;
        }

        throw new BadRequestException(ErrorCodes.WEBSITE_QUALYS_NOT_ENABLED.name());
    }

    public WebApp read(Long id) throws QualysException {

        return read(id, "/get/was/webapp/");
    }

    public WebApp update(Long id, WebApp webApp) throws QualysException, Website4sgCoreException {

        WebsiteEntity website = websiteRepository.findByCode(webApp.getName()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        if (BooleanUtils.isTrue(website.getIsQualysEnable())) {

            DomainEntity domain = domainRepository.findByCode(webApp.getUrl()).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.DOMAIN_NOT_FOUND.name()));

            if (BooleanUtils.isNotTrue(domain.getIsQualysEnable())) {
                throw new BadRequestException(ErrorCodes.DOMAIN_QUALYS_NOT_ENABLED.name());
            }

            StringBuilder url = new StringBuilder();

            if (BooleanUtils.isTrue(domain.getHttpsEnable())) {
                url.append("https://");
            } else {
                url.append("http://");
            }

            url.append(domain.getCode());

            webApp.setUrl(url.toString());

            WebApp qualysWebApp = update(id, webApp, "/update/was/webapp/");
            website.setQualysWebAppId(qualysWebApp.getId());
            website.setLastUpdate(new Date());
            websiteRepository.save(website);
            return qualysWebApp;
        }

        throw new BadRequestException(ErrorCodes.WEBSITE_QUALYS_NOT_ENABLED.name());
    }

    public void delete(Long id) throws QualysException, Website4sgCoreException {

        WebsiteEntity website = websiteRepository.findByQualysWebAppId(id).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        delete(id, "/delete/was/webapp/");

        website.setQualysWebAppId(null);
        websiteRepository.save(website);
    }

    public Page<WebApp> getAll(Pageable pageable) throws QualysException {

        Specification<WebsiteEntity> spec = Specification.where(WebsiteSpecification.qualysWebAppIdNotNull());
        spec = spec.and(WebsiteSpecification.qualysEnable(true));
        Page<WebsiteEntity> webAppsWsip = websiteRepository.findAll(spec, pageable);

        String websiteIds = StringUtils.EMPTY;
        websiteIds = webAppsWsip.getContent().stream().map(website -> website.getQualysWebAppId()).collect(
                Collectors.toList()).stream().map(id -> id.toString()).collect(Collectors.joining(","));

        Criteria websiteIdsCriteria = new Criteria("id", "IN", websiteIds);

        Filters filters = Filters.builder().criteria(Arrays.asList(websiteIdsCriteria)).build();

        Page<WebApp> webApps = search(pageable, filters, "/search/was/webapp");

        // Refresh WebApps Wsip from Qualys in async
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        forkJoinPool.execute(() -> webApps.getContent().parallelStream().forEach(scan -> {
            try {
                read(scan.getId());
            } catch (QualysException e) {
                log.warn(e.getMessage());
            }
        }));

        Page<WebApp> webAppsFiltered = new PageImpl<WebApp>(webApps.getContent(), pageable, webAppsWsip
                .getTotalElements());

        return webAppsFiltered;
    }

    public Page<WebApp> search(Pageable pageable, Filters filters) throws QualysException {
        Page<WebApp> webApps = search(pageable, filters, "/search/was/webapp");

        List<WebApp> webAppList = webApps.getContent().stream().filter(webApp -> websiteRepository.findByCode(webApp
                .getName()).isPresent()).map(webApp -> {

                    WebsiteEntity website = websiteRepository.findByCode(webApp
                            .getName()).get();

                    if (website.getQualysWebAppId() == null) {
                        website.setQualysWebAppId(webApp.getId());
                        websiteRepository.save(website);
                    }

                    return webApp;

                }).collect(Collectors.toList());

        Integer numberFiltered = webApps.getContent().size() - webAppList.size();

        Long countFiltered = webApps.getTotalElements() - numberFiltered;

        Page<WebApp> webAppsFiltered = new PageImpl<WebApp>(webAppList, pageable, countFiltered);

        return webAppsFiltered;
    }

}
