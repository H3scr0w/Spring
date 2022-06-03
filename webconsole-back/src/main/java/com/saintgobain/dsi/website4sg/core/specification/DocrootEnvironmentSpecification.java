package com.saintgobain.dsi.website4sg.core.specification;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.RolesEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class DocrootEnvironmentSpecification {

    public static Specification<DocrootEnvironmentEntity> joinWithUserRoles(ProjectTypeId projectType,
            List<String> userRoles) {

        return (Specification<DocrootEnvironmentEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<ProjectEntity, AccessRightEntity> rights = null;

            switch (projectType) {
            case D_DOCROOTCORE:
                Join<DocrootEnvironmentEntity, DrupalDocrootCoreEntity> drupal = root.join("drupaldocrootcore",
                        JoinType.LEFT);
                Join<DrupalDocrootCoreEntity, ProjectEntity> drupalproject = drupal.join("project", JoinType.LEFT);
                rights = drupalproject.join("accessrightByProject", JoinType.LEFT);
                break;
            case D_WEBSITE:
                Join<DocrootEnvironmentEntity, WebsiteDeployedEntity> websitesDeployed = root.join(
                        "websitedeployedByDocrootEnvironment", JoinType.LEFT);
                Join<WebsiteDeployedEntity, WebsiteEntity> website = websitesDeployed.join(
                        "website", JoinType.LEFT);
                Join<WebsiteEntity, ProjectEntity> webproject = website.join("project", JoinType.LEFT);
                rights = webproject.join("accessrightByProject", JoinType.LEFT);
                break;
            }

            if (rights != null) {
                Join<AccessRightEntity, RolesEntity> roles = rights.join("roles", JoinType.LEFT);
                return builder.in(roles.get("label")).value(
                        userRoles);
            } else {
                return builder.and();
            }

        };
    }

    public static Specification<DocrootEnvironmentEntity> joinWithUser(ProjectTypeId projectType, String userEmail) {

        return (Specification<DocrootEnvironmentEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<ProjectEntity, AccessRightEntity> rights = null;

            switch (projectType) {
            case D_DOCROOTCORE:
                Join<DocrootEnvironmentEntity, DrupalDocrootCoreEntity> drupal = root.join("drupaldocrootcore",
                        JoinType.LEFT);
                Join<DrupalDocrootCoreEntity, ProjectEntity> drupalproject = drupal.join("project", JoinType.LEFT);
                rights = drupalproject.join("accessrightByProject", JoinType.LEFT);
                break;
            case D_WEBSITE:
                Join<DocrootEnvironmentEntity, WebsiteDeployedEntity> websitesDeployed = root.join(
                        "websitedeployedByDocrootEnvironment", JoinType.LEFT);
                Join<WebsiteDeployedEntity, WebsiteEntity> website = websitesDeployed.join(
                        "website", JoinType.LEFT);
                Join<WebsiteEntity, ProjectEntity> webproject = website.join("project", JoinType.LEFT);
                rights = webproject.join("accessrightByProject", JoinType.LEFT);
                break;
            }

            if (rights != null) {
                Join<AccessRightEntity, UsersEntity> users = rights.join("users", JoinType.LEFT);
                return builder.equal(users.get("email"), userEmail);
            } else {
                return builder.and();
            }

        };
    }

    public static Specification<DocrootEnvironmentEntity> docrootId(Long docrootId) {
        return (Specification<DocrootEnvironmentEntity>) (root, query, builder) -> builder.equal(root.get(
                "docrootId"), docrootId);
    }
}
