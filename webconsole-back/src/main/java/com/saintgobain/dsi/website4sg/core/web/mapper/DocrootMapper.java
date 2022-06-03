package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootBody;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader.DocrootHeaderBuilder;
import com.saintgobain.dsi.website4sg.core.web.bean.DomainHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.EnvironmentDetail;

public class DocrootMapper {

    public static DocrootHeader toDocrootHeader(DocrootEntity docroot, boolean isAdmin) {
        if (docroot == null) {
            return null;
        }
        DocrootHeaderBuilder builder = DocrootHeader.builder()
                .name(docroot.getName())
                .code(docroot.getCode());

        if (isAdmin) {
            builder.rundeckJobApiUrl(docroot.getRundeckJobApiUrl())
                    .providerInternalId(docroot.getProviderInternalId());
        }

        if (docroot.getHostingprovider() != null) {

            builder.hostingProviderCode(docroot.getHostingprovider().getCode())
                    .hostingProviderName(docroot.getHostingprovider().getName());
        }

        return builder.build();

    }

    public static DocrootBody toDocrootBody(DocrootEntity docroot, List<WebsiteDeployedEntity> websites,
            boolean isAdmin) {

        DocrootBody.DocrootBodyBuilder docrootBodyBuilder = DocrootBody.builder()
                .code(docroot.getCode())
                .name(docroot.getName());

        if (isAdmin) {
            docrootBodyBuilder.rundeckJobApiUrl(docroot.getRundeckJobApiUrl())
                    .providerInternalId(docroot.getProviderInternalId());
        }

        if (docroot.getHostingprovider() != null) {

            docrootBodyBuilder.hostingProviderCode(docroot.getHostingprovider().getCode()).hostingProviderName(docroot
                    .getHostingprovider().getName());
        }

        if (!CollectionUtils.isEmpty(websites)) {
            List<EnvironmentDetail> environments = new ArrayList<>();
            websites.stream().map(website -> {

                EnvironmentDetail.EnvironmentDetailBuilder environmentBuilder = EnvironmentDetail.builder()
                        .websiteVersion(website.getWebsiteVersion())
                        .cmsVersion(website.getDocrootenvironmentByWebsiteDeployed().getCmsVersion())
                        .name(website.getDocrootenvironmentByWebsiteDeployed().getEnvironment().getName())
                        .environmentCode(website.getDocrootenvironmentByWebsiteDeployed().getEnvironment().getCode()
                                .toLowerCase());


                if (website.getDocrootenvironmentByWebsiteDeployed().getCms() != null) {

                    environmentBuilder.cmsCode(website.getDocrootenvironmentByWebsiteDeployed().getCms().getCode())
                            .cmsName(website.getDocrootenvironmentByWebsiteDeployed().getCms().getName());
                }

                if (!CollectionUtils.isEmpty(website.getDomains())) {
                    List<DomainHeader> domains = new ArrayList<>();

                    website.getDomains().stream().map(domain -> DomainMapper.toDomainHeader(domain)).forEach(
                            domains::add);
                    environmentBuilder.domains(domains);

                }

                return environmentBuilder.build();

            }).forEach(environments::add);

            docrootBodyBuilder.environments(environments);
        }

        return docrootBodyBuilder.build();
    }

    public static List<DocrootHeader> toDocrootHeaderList(List<DocrootEntity> docroots, boolean isAdmin) {
        return docroots.stream().map(d -> toDocrootHeader(d, isAdmin)).collect(Collectors.toList());
    }

    public static Page<DocrootHeader> toDocrootHeaderList(Page<DocrootEntity> docroots, boolean isAdmin) {
        List<DocrootHeader> docrootList = docroots.getContent().stream().map(cms -> toDocrootHeader(cms, isAdmin))
                .collect(
                        Collectors
                                .toList());
        return new PageImpl<DocrootHeader>(docrootList, docroots.getPageable(), docroots.getTotalElements());
    }

}