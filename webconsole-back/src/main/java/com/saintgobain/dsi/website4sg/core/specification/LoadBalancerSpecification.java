package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.LoadBalancerEntity;

public class LoadBalancerSpecification {

    public static Specification<LoadBalancerEntity> joinWithDocrootEnv(Long docrootEnvId) {

        return (Specification<LoadBalancerEntity>) (root, query, builder) -> {
            query.distinct(true);
            Join<LoadBalancerEntity, DocrootEnvironmentEntity> docrootenvironment = root.join("docrootenvironment");
            return builder.equal(docrootenvironment.get("docrootEnvironmentId"), docrootEnvId);
        };
    }

    public static Specification<LoadBalancerEntity> nameSpecification(String name) {
        return (Specification<LoadBalancerEntity>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "name")),
                name.toLowerCase() + "%");
    }

}
