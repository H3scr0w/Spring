package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class UserSpecification {

    public static Specification<UsersEntity> joinWithProject(Long projectId) {
        return new Specification<UsersEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<UsersEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<UsersEntity, AccessRightEntity> rights = root.join("accessrightByUsers");
                Join<AccessRightEntity, ProjectEntity> project = rights.join("project");
                return cb.equal(project.get("projectId"), projectId);

            }
        };
    }
}
