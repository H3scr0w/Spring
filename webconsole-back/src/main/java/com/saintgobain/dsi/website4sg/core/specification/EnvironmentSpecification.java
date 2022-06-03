package com.saintgobain.dsi.website4sg.core.specification;

import java.util.List;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.RolesEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class EnvironmentSpecification {

    public static Specification<EnvironmentEntity> joinWithUserRoles(ProjectTypeId projectType,
            List<String> userRoles, String userEmail) {

        return (Specification<EnvironmentEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<EnvironmentEntity, DocrootEnvironmentEntity> docrootEnvs = root.join("docrootenvironmentByServer");
            Join<ProjectEntity, AccessRightEntity> rights = null;

            switch (projectType) {
            case D_DOCROOTCORE:
                Join<DocrootEnvironmentEntity, DrupalDocrootCoreEntity> drupal = docrootEnvs.join("drupaldocrootcore");
                Join<DrupalDocrootCoreEntity, ProjectEntity> drupalproject = drupal.join("project");
                rights = drupalproject.join("accessrightByProject");
                break;
            case D_WEBSITE:
                Join<DocrootEnvironmentEntity, WebsiteDeployedEntity> websitesDeployed = docrootEnvs.join(
                        "websitedeployedByDocrootEnvironment");
                Join<WebsiteDeployedEntity, WebsiteEntity> website = websitesDeployed.join(
                        "website");
                Join<WebsiteEntity, ProjectEntity> webproject = website.join("project");
                rights = webproject.join("accessrightByProject");
                break;
            }

            if (rights != null) {
                Join<AccessRightEntity, RolesEntity> roles = rights.join("roles");
                Join<AccessRightEntity, UsersEntity> users = rights.join("users");
                return builder.and(builder.equal(users.get("email"), userEmail), builder.in(roles.get("label")).value(
                        userRoles));
            } else {
                return builder.and();
            }

        };
    }

    public static Specification<EnvironmentEntity> nameSpecification(String name) {
        return (Specification<EnvironmentEntity>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "name")),
                name.toLowerCase() + "%");
    }

}
