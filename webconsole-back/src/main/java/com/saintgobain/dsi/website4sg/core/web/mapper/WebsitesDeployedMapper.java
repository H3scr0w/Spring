package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsitesDeployedHeader;

public class WebsitesDeployedMapper {

    public static WebsitesDeployedHeader toWebsitesDeployedHeader(WebsiteDeployedEntity websiteDeployedEntity) {
        if (websiteDeployedEntity == null) {
            return null;
        }
        return WebsitesDeployedHeader.builder()
                .websiteId(websiteDeployedEntity.getWebsite() != null ? websiteDeployedEntity.getWebsite()
                        .getWebsiteId() : null)
                .websiteVersion(websiteDeployedEntity.getWebsiteVersion())
                .websiteName(websiteDeployedEntity.getWebsite() != null ? websiteDeployedEntity.getWebsite().getName()
                        : "")
                .code(websiteDeployedEntity.getWebsite() != null ? websiteDeployedEntity.getWebsite().getCode() : "")
                .build();
    }


    public static Page<WebsitesDeployedHeader> toWebsitesDeployedPage(
            Page<WebsiteDeployedEntity> websiteDeployedEntities) {

        List<WebsitesDeployedHeader> websitesDeployedHeader = websiteDeployedEntities.getContent().stream().map(
                websiteEntity -> toWebsitesDeployedHeader(websiteEntity)).collect(Collectors.toList());

        return new PageImpl<WebsitesDeployedHeader>(websitesDeployedHeader, websiteDeployedEntities.getPageable(),
                websiteDeployedEntities.getTotalElements());
    }

}
