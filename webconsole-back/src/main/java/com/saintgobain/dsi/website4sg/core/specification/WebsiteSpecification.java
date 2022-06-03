package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class WebsiteSpecification {

    public static Specification<WebsiteEntity> joinWithUser(String userEmail) {

        return (Specification<WebsiteEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<WebsiteEntity, ProjectEntity> project = root.join("project");
            Join<ProjectEntity, AccessRightEntity> rights = project.join("accessrightByProject");
            Join<AccessRightEntity, UsersEntity> users = rights.join("users");
            return builder.equal(users.get("email"), userEmail);
        };
    }

    public static Specification<WebsiteEntity> name(String name) {
        return (Specification<WebsiteEntity>) (root, query, builder) -> builder.like(builder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }

    public static Specification<WebsiteEntity> qualysEnable(Boolean qualysEnable) {
        return (Specification<WebsiteEntity>) (root, query, builder) -> builder.equal(root.get("isQualysEnable"),
                qualysEnable);
    }

    public static Specification<WebsiteEntity> qualysWebAppIdNotNull() {
        return (Specification<WebsiteEntity>) (root, query, builder) -> builder.isNotNull(root.get("qualysWebAppId"));
    }

    public static Specification<WebsiteEntity> enable(Boolean enable) {
        return (Specification<WebsiteEntity>) (root, query, builder) -> builder.equal(root.get("enable"),
                enable);
    }

}
