package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.ProjectBody;

public class ProjectMapper {

    public static ProjectBody toProjectBody(ProjectEntity projectEntity, boolean isAdmin) {
        if (projectEntity == null) {
            return null;
        }
        return ProjectBody.builder()
                .projectId(projectEntity.getProjectId())
                .drupaldocrootcore(DrupalDocrootCoreMapper.toDrupalDocrootCoreBody(projectEntity
                        .getDrupaldocrootcore(), isAdmin))
                .website(WebsiteMapper.toWebsiteBody(projectEntity.getWebsite(), isAdmin))
                .projectCode(projectEntity.getDrupaldocrootcore() != null ? projectEntity.getDrupaldocrootcore()
                        .getCode()
                        : projectEntity.getWebsite().getCode())
                .projectName(projectEntity.getDrupaldocrootcore() != null ? projectEntity.getDrupaldocrootcore()
                        .getName()
                        : projectEntity.getWebsite().getName())
                .projectType(projectEntity.getDrupaldocrootcore() != null ? ProjectTypeId.D_DOCROOTCORE.getName()
                        : ProjectTypeId.D_WEBSITE.getName())
                .build();
    }

    public static Page<ProjectBody> toProjectBodyPage(Page<ProjectEntity> projectEntities, boolean isAdmin) {
        List<ProjectBody> projectBodies = projectEntities.getContent().stream().map(project -> toProjectBody(project,
                isAdmin)).collect(Collectors.toList());

        return new PageImpl<ProjectBody>(projectBodies, projectEntities.getPageable(), projectEntities
                .getTotalElements());
    }
}