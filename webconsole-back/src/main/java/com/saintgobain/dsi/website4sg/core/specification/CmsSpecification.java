package com.saintgobain.dsi.website4sg.core.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;

public class CmsSpecification {

    public static Specification<CmsEntity> name(String name) {
        return (Specification<CmsEntity>) (root, query, builder) -> builder.like(builder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }

    public static Specification<CmsEntity> enable(Boolean enable) {
        return (Specification<CmsEntity>) (root, query, builder) -> builder.equal(root.get("enable"),
                enable);
    }

}
