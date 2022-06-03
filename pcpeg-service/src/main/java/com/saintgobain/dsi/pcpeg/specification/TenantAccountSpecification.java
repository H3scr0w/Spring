package com.saintgobain.dsi.pcpeg.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;

public class TenantAccountSpecification {

    public static Specification<PeRefTeneurCompte> labelSpecification(String label) {
        return (Specification<PeRefTeneurCompte>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "teneurCompteLibelle")), "%" + label.trim()
                        .toLowerCase() + "%");
    }

}
