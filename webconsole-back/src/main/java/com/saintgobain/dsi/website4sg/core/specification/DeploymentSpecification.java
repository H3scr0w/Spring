package com.saintgobain.dsi.website4sg.core.specification;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class DeploymentSpecification {

    public static Specification<DeploymentEntity> joinWithUser(String userEmail) {

        return (Specification<DeploymentEntity>) (root, query, cb) -> {
            Join<DeploymentEntity, ProjectEntity> project = root.join("project");
            Join<ProjectEntity, AccessRightEntity> rights = project.join("accessrightByProject");
            Join<AccessRightEntity, UsersEntity> users = rights.join("users");
            return cb.equal(users.get("email"), userEmail);
        };
    }

    public static Specification<DeploymentEntity> withStatus(List<String> statusList) {
        return (Specification<DeploymentEntity>) (root, query, cb) -> {
            final Path<String> status = root.<String> get("deploymentStatusId");
            return status.in(statusList);
        };
    }

}
