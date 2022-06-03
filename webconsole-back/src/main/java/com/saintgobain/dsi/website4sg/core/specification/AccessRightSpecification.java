package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class AccessRightSpecification {

    public static Specification<AccessRightEntity> joinWithUser(String userEmail) {

        return (Specification<AccessRightEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<AccessRightEntity, UsersEntity> users = root.join("users");
            return builder.equal(users.get("email"), userEmail);
        };
    }

}
