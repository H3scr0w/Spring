package com.saintgobain.dsi.website4sg.core.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;

public class HostingProviderSpecification {

    public static Specification<HostingProviderEntity> nameSpecification(String name) {
        return (Specification<HostingProviderEntity>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "name")),
                name.toLowerCase() + "%");
    }

}
