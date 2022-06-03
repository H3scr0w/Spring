package com.saintgobain.dsi.pcpeg.specification;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.CampaignView;

public class CampaignViewSpecification {

    public static Specification<CampaignView> currentYearSpecification() {
        return (Specification<CampaignView>) (root, query, builder) -> builder.isTrue(root.get("flagEnCours"));
    }

    public static Specification<CampaignView> yearSpecification(Short year) {
        return (Specification<CampaignView>) (root, query, builder) -> builder.equal(root.get("id").get("anneeId"),
                year);
    }

    public static Specification<CampaignView> formulaireOneSpecification() {
        return (Specification<CampaignView>) (root, query, builder) -> {
            return builder.equal(root.get("id").get("formulaireId"), 1);
        };
    }

    public static Specification<CampaignView> correspondantSgidSpecification(String sgid) {
        return (Specification<CampaignView>) (root, query, builder) -> {
            return builder.equal(root.get("correspondantActuelSgid"), sgid);
        };
    }

    public static Specification<CampaignView> statusIdSpecification(Short parsedStatutId) {
        return (Specification<CampaignView>) (root, query, builder) -> builder.equal(root.get("statutId"),
                parsedStatutId);
    }

}
