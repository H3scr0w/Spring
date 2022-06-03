package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.CollectionUtils;

import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain.DomainBuilder;
import com.saintgobain.dsi.website4sg.core.web.bean.DomainHeader;

public class DomainMapper {

    public static Domain toDomain(DomainEntity domainEntity, boolean isAdmin) {
        if (domainEntity == null) {
            return null;
        }

        DomainBuilder builder = Domain.builder()
                .code(domainEntity.getCode())
                .name(domainEntity.getName())
                .domainType(domainEntity.getDomainType().getDomainTypeId())
                .isQualysEnable(domainEntity.getIsQualysEnable())
                .httpsEnable(domainEntity.getHttpsEnable())
                .isMonitorEnable(domainEntity.getIsMonitorEnable());

        if (domainEntity.getWebsiteDeployed() != null) {
            builder.websiteCode(domainEntity.getWebsiteDeployed().getWebsite()
                    .getCode())
                    .docrootCode(domainEntity.getWebsiteDeployed().getDocrootenvironmentByWebsiteDeployed().getDocroot()
                            .getCode())
                    .environmentCode(domainEntity.getWebsiteDeployed().getDocrootenvironmentByWebsiteDeployed()
                            .getEnvironment().getCode().toLowerCase());
        }

        if (domainEntity.getParent() != null) {
            builder.parent(toDomainHeader(domainEntity.getParent()));
        }

        if (!CollectionUtils.isEmpty(domainEntity.getChildren())) {
            builder.children(toDomainList(domainEntity.getChildren(), isAdmin));
        }

        if (isAdmin) {

            if (domainEntity.getRegistar() != null) {
                builder.registarCode(domainEntity.getRegistar().getCode())
                        .registarName(domainEntity.getRegistar().getName());
            }

            builder.qualysWebAppId(domainEntity.getQualysWebAppId())
                    .qualysWebAuthId(domainEntity.getQualysWebAuthId())
                    .wafId(domainEntity.getWafId())
                    .isBasicAuth(domainEntity.getIsBasicAuth())
                    .realm(domainEntity.getRealm())
                    .user(domainEntity.getUser())
                    .password(domainEntity.getPassword())
                    .useDocrootEnvAuth(domainEntity.getUseDocrootEnvAuth())
                    .wafId(domainEntity.getWafId())
                    .monitorKeyword(domainEntity.getMonitorKeyword());
        }

        return builder.build();
    }

    public static Page<Domain> toDomainList(Page<DomainEntity> domainEntities, boolean isAdmin) {
        List<Domain> domainList = domainEntities.getContent()
                .stream()
                .map(domainEntity -> toDomain(domainEntity, isAdmin))
                .collect(Collectors.toList());

        return new PageImpl<Domain>(domainList, domainEntities.getPageable(), domainEntities.getTotalElements());

    }

    public static List<Domain> toDomainList(List<DomainEntity> domainEntities, boolean isAdmin) {
        List<Domain> domainList = domainEntities.stream()
                .map(domainEntity -> toDomain(domainEntity, isAdmin))
                .collect(Collectors.toList());
        return domainList;

    }

    public static DomainEntity toDomainEntity(Domain domain) {

        DomainEntity.DomainEntityBuilder builder = DomainEntity.builder();
        return mapToDomainEntity(builder, domain);
    }

    public static DomainEntity toNewDomainEntity(DomainEntity domainEntity, Domain domain) {

        DomainEntity.DomainEntityBuilder builder = domainEntity.toBuilder();
        return mapToDomainEntity(builder, domain);
    }

    public static DomainHeader toDomainHeader(DomainEntity domain) {
        if (domain == null) {
            return null;
        }

        DomainHeader.DomainHeaderBuilder builder = DomainHeader.builder();
        builder.code(domain.getCode())
                .name(domain.getName())
                .domainType(domain.getDomainType().getDomainTypeId())
                .httpsEnable(domain.getHttpsEnable());

        DomainEntity parent = domain.getParent();

        if (parent != null) {

            builder.parent(toDomainHeader(parent));
        }

        return builder.build();

    }

    private static DomainEntity mapToDomainEntity(DomainEntity.DomainEntityBuilder builder, Domain domain) {
        if (domain == null) {
            return null;
        }

        return builder
                .name(domain.getName())
                .isBasicAuth(domain.getIsBasicAuth())
                .realm(domain.getRealm())
                .user(domain.getUser())
                .password(domain.getPassword())
                .isQualysEnable(domain.getIsQualysEnable())
                .qualysWebAppId(domain.getQualysWebAppId())
                .qualysWebAuthId(domain.getQualysWebAuthId())
                .useDocrootEnvAuth(domain.getUseDocrootEnvAuth())
                .wafId(domain.getWafId())
                .httpsEnable(domain.getHttpsEnable())
                .isMonitorEnable(domain.getIsMonitorEnable())
                .monitorKeyword(domain.getMonitorKeyword())
                .build();
    }

}
