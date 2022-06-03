package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DomainEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteDeployedEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class DomainSpecification {

    public static Specification<DomainEntity> joinWithUser(String userEmail) {

        return (Specification<DomainEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<DomainEntity, WebsiteDeployedEntity> websiteDeployed = root.join("websiteDeployed");
            Join<WebsiteDeployedEntity, WebsiteEntity> website = websiteDeployed.join("website");
            Join<WebsiteEntity, ProjectEntity> project = website.join("project");
            Join<ProjectEntity, AccessRightEntity> rights = project.join("accessrightByProject");
            Join<AccessRightEntity, UsersEntity> users = rights.join("users");
            return builder.equal(users.get("email"), userEmail);
        };
    }

    public static Specification<DomainEntity> name(String name) {
        return (Specification<DomainEntity>) (root, query, builder) -> builder.like(builder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }

    public static Specification<DomainEntity> qualysEnable(Boolean qualysEnable) {
        return (Specification<DomainEntity>) (root, query, builder) -> builder.equal(root.get("isQualysEnable"),
                qualysEnable);
    }

    public static Specification<DomainEntity> joinWithDocrootEnv(Long docrootEnvId) {

        return (Specification<DomainEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<DomainEntity, DocrootEnvironmentEntity> docrootenvironment = root.join("docrootenvironment");
            return builder.equal(docrootenvironment.get("docrootEnvironmentId"), docrootEnvId);
        };
    }

    public static Specification<DomainEntity> joinWithWebsite(String websiteCode) {

        return (Specification<DomainEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<DomainEntity, WebsiteDeployedEntity> websiteDeployed = root.join("websiteDeployed");
            Join<WebsiteDeployedEntity, WebsiteEntity> website = websiteDeployed.join("website");
            return builder.equal(website.get("code"), websiteCode);
        };
    }

    public static Specification<DomainEntity> wafIdNotNull() {
        return (Specification<DomainEntity>) (root, query, builder) -> builder.isNotNull(root.get(
                "wafId"));
    }

    public static Specification<DomainEntity> parentNull() {
        return (Specification<DomainEntity>) (root, query, builder) -> builder.isNull(root.get(
                "parent"));
    }

    public static Specification<DomainEntity> domainTypeId(String domainTypeId) {
        return (Specification<DomainEntity>) (root, query, builder) -> builder.equal(builder.lower(root.get(
                "domainTypeId")),
                domainTypeId.toLowerCase());
    }

}
