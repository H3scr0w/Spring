package com.saintgobain.dsi.pcpeg.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;

public class CompanySpecification {

    private CompanySpecification() {}

    public static Specification<PeDimSociete> companyActiveSpecification() {
        return (Specification<PeDimSociete>) (root, query, builder) -> {
            query.distinct(true);
            return builder.equal(root.get("active"), 1);
        };
    }

    public static Specification<PeDimSociete> companyflagAdherenteSpecification() {
        return (Specification<PeDimSociete>) (root, query, builder) -> builder.equal(root.get("flagAdherente"), 1);
    }

    public static Specification<PeDimSociete> companySgidSpecification(String sgid) {
        return (Specification<PeDimSociete>) (root, query, builder) -> {
            Join<PeDimSociete, PeParSociete> peParSocietes = root.join("peParSocietes");
            Join<PeParSociete, PeDimUtilisateurs> peDimUtilisateurs = peParSocietes.join("peDimUtilisateurs");
            return builder.equal(peDimUtilisateurs.get("sgid"), sgid);
        };
    }

    public static Specification<PeDimSociete> codeSifSpecification(String codeSif) {
        return (Specification<PeDimSociete>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "codeSif")), "%" + codeSif.trim()
                .toLowerCase() + "%");
    }

}
