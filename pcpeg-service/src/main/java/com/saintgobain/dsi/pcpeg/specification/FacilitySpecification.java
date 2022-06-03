package com.saintgobain.dsi.pcpeg.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;

public class FacilitySpecification {

    public static Specification<PeDimEtablissement> labelSpecification(String label) {
        return (Specification<PeDimEtablissement>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "facilityLabel")), "%" + label.trim()
                        .toLowerCase() + "%");
    }

    public static Specification<PeDimEtablissement> activeSpecification() {
        return (Specification<PeDimEtablissement>) (root, query, builder) -> builder.equal(builder.upper(root.get(
                "isActive")), "O");
    }

    public static Specification<PeDimEtablissement> societeSidSpecification(Integer societeSid) {
        return (Specification<PeDimEtablissement>) (root, query, builder) -> builder.equal(root.get(
                "id").get("societeSid"), societeSid);
    }

    public static Specification<PeDimEtablissement> codeSifSpecification(String codeSif) {
        return (Specification<PeDimEtablissement>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "societe").get("codeSif")), codeSif.trim().toLowerCase() + "%");
    }

    public static Specification<PeDimEtablissement> companyLabelSpecification(String companyLabel) {
        return (Specification<PeDimEtablissement>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "societe").get("societeLibelle")), "%" + companyLabel.trim().toLowerCase() + "%");
    }

}
