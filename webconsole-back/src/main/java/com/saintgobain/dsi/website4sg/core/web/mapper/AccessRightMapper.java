package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.AccessRightBody;

public class AccessRightMapper implements Function<AccessRightEntity, AccessRightBody> {

    public static AccessRightBody toAccessRightBody(AccessRightEntity accessRightEntity) {
        if (accessRightEntity == null) {
            return null;
        }

        String projectCode = StringUtils.EMPTY;
        String projectName = StringUtils.EMPTY;
        String projectType = StringUtils.EMPTY;

        if (accessRightEntity.getProject().getDrupaldocrootcore() != null) {
            DrupalDocrootCoreEntity ddc = accessRightEntity.getProject().getDrupaldocrootcore();
            projectCode = ddc.getCode();
            projectName = ddc.getName();
            projectType = ProjectTypeId.D_DOCROOTCORE.getName();
        } else {
            WebsiteEntity website = accessRightEntity.getProject().getWebsite();
            projectCode = website.getCode();
            projectName = website.getName();
            projectType = ProjectTypeId.D_WEBSITE.getName();
        }

        return AccessRightBody.builder()
                .accessRightId(accessRightEntity.getAccessRightId())
                .projectCode(projectCode)
                .projectName(projectName)
                .projectType(projectType)
                .roleLabel(accessRightEntity.getRoles().getLabel())
                .userMail(accessRightEntity.getUsers().getEmail())
                .build();
    }

    public static List<AccessRightBody> toAccessRightBodyList(List<AccessRightEntity> accessRights) {
        return accessRights.stream().map(r -> toAccessRightBody(r)).collect(Collectors.toList());
    }

    @Override
    public AccessRightBody apply(AccessRightEntity right) {
        return toAccessRightBody(right);
    }

}