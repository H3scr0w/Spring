package com.saintgobain.dsi.pcpeg.specification;

import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class AuthoritySettingSpecification {

    public static Specification<PeParHabilitations> yearSpecification(Short year) {
        return (Specification<PeParHabilitations>) (root, query, builder) -> {
            Join<PeParHabilitations, PeParSociete> peParSocietes = root.join("peParSociete");

            return builder.equal(peParSocietes.get("id").get("anneeId"),year);
        };
    }
    public static Specification<PeParHabilitations> societeSpecification(Integer societeId) {
        return (Specification<PeParHabilitations>) (root, query, builder) -> {
            Join<PeParHabilitations, PeParSociete> peParSocietes = root.join("peParSociete");

            return builder.equal(peParSocietes.get("id").get("societeSid"),societeId);
        };
    }

    public static Specification<PeParHabilitations> companySgidSpecification(String sgid) {
        return (Specification<PeParHabilitations>) (root, query, builder) -> {
            Join<PeParHabilitations, PeParSociete> peParSocietes = root.join("peParSociete");
            Join<PeParSociete, PeDimUtilisateurs> peDimUtilisateurs = peParSocietes.join("peDimUtilisateurs");
            return builder.equal(peDimUtilisateurs.get("sgid"), sgid);
        };
    }
}
