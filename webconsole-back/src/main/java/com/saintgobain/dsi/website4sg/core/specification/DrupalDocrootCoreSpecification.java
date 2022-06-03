package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class DrupalDocrootCoreSpecification {

    public static Specification<DrupalDocrootCoreEntity> joinWithUser(String userEmail) {

        return (Specification<DrupalDocrootCoreEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<DrupalDocrootCoreEntity, ProjectEntity> project = root.join("project");
            Join<ProjectEntity, AccessRightEntity> rights = project.join("accessrightByProject");
            Join<AccessRightEntity, UsersEntity> users = rights.join("users");
            return builder.equal(users.get("email"), userEmail);
        };
    }

    public static Specification<DrupalDocrootCoreEntity> nameSpecification(String name) {
        return (Specification<DrupalDocrootCoreEntity>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "name")),
                name.toLowerCase() + "%");
    }

}
