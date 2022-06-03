package com.saintgobain.dsi.website4sg.core.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.RegistarEntity;

public class RegistarSpecification {

    public static Specification<RegistarEntity> nameSpecification(String name) {
        return (Specification<RegistarEntity>) (root, query, builder) -> builder.like(builder.lower(root.get("name")),
                name.toLowerCase() + "%");
    }

}
