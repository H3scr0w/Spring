package com.saintgobain.dsi.website4sg.core.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.CertificateEntity;

public class CertificateSpecification {

    public static Specification<CertificateEntity> name(String name) {
        return (Specification<CertificateEntity>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "name")),
                name.toLowerCase() + "%");
    }
}
