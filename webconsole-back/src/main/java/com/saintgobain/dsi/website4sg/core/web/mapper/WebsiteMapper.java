package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Website;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteBody;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteBody.WebsiteBodyBuilder;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody.WebsiteDetailBodyBuilder;

public class WebsiteMapper {

    public static Page<WebsiteDetailBody> toWebsiteDetailBodyPage(Page<WebsiteEntity> websitesEntities,
            boolean isAdmin) {

        List<WebsiteDetailBody> websites = websitesEntities.getContent().stream().map(website -> toWebsiteDetailBody(
                website, isAdmin)).collect(Collectors
                        .toList());
        return new PageImpl<WebsiteDetailBody>(websites, websitesEntities.getPageable(), websitesEntities
                .getTotalElements());
    }

    public static WebsiteDetailBody toWebsiteDetailBody(WebsiteEntity website, boolean isAdmin) {
        if (website == null) {
            return null;
        }

        WebsiteDetailBodyBuilder builder = WebsiteDetailBody.builder()
                .codeRepositoryUrl(website.getCodeRepositoryUrl())
                .homeDirectory(website.getHomeDirectory())
                .enable(website.getEnable())
                .created(website.getCreated())
                .lastUpdate(website.getLastUpdate())
                .name(website.getName())
                .code(website.getCode())
                .isLive(website.getIsLive());

        if (isAdmin) {
            builder.qualysWebAppId(website.getQualysWebAppId())
                    .binaryRepositoryUrl(website.getBinaryRepositoryUrl())
                    .isQualysEnable(website.getIsQualysEnable());
        }

        return builder.build();

    }

    public static WebsiteEntity toWebsiteEntity(Website website, String websiteCode) {
        return WebsiteEntity.builder()
                .codeRepositoryUrl(website.getCodeRepositoryUrl())
                .binaryRepositoryUrl(website.getBinaryRepositoryUrl())
                .homeDirectory(website.getHomeDirectory())
                .enable(website.getEnable())
                .code(websiteCode)
                .name(website.getName())
                .qualysWebAppId(website.getQualysWebAppId())
                .isQualysEnable(website.getIsQualysEnable())
                .created(new Date())
                .lastUpdate(new Date())
                .isLive(website.getIsLive())
                .build();
    }

    public static WebsiteEntity toNewWebsiteEntity(WebsiteEntity websiteEntity, Website website) {
        if (website == null) {
            return null;
        }

        return websiteEntity.toBuilder()
                .codeRepositoryUrl(website.getCodeRepositoryUrl())
                .binaryRepositoryUrl(website.getBinaryRepositoryUrl())
                .homeDirectory(website.getHomeDirectory())
                .enable(website.getEnable())
                .name(website.getName())
                .qualysWebAppId(website.getQualysWebAppId())
                .isQualysEnable(website.getIsQualysEnable())
                .lastUpdate(new Date())
                .isLive(website.getIsLive())
                .build();
    }

    public static WebsiteBody toWebsiteBody(WebsiteEntity website, boolean isAdmin) {
        if (website == null) {
            return null;
        }

        WebsiteBodyBuilder builder = WebsiteBody.builder()
                .websiteId(website.getWebsiteId())
                .codeRepositoryUrl(website.getCodeRepositoryUrl())
                .homeDirectory(website.getHomeDirectory())
                .name(website.getName())
                .enable(website.getEnable())
                .created(website.getCreated())
                .lastUpdate(website.getLastUpdate())
                .code(website.getCode())
                .isLive(website.getIsLive());

        if (isAdmin) {
            builder.qualysWebAppId(website.getQualysWebAppId())
                    .binaryRepositoryUrl(website.getBinaryRepositoryUrl())
                    .isQualysEnable(website.getIsQualysEnable());
        }

        return builder.build();
    }

}