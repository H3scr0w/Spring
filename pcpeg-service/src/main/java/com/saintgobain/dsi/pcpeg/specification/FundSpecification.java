package com.saintgobain.dsi.pcpeg.specification;

import java.util.List;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimGrpFonds;

public class FundSpecification {

    public static Specification<PeDimFonds> labelSpecification(String label) {
        return (Specification<PeDimFonds>) (root, query, builder) -> builder.like(builder.lower(root.get(
                "fondsLibelle")), "%" + label.trim()
                        .toLowerCase() + "%");
    }

    public static Specification<PeDimFonds> groupsSpecification(List<String> groups) {
        Specification<PeDimFonds> spec = Specification.where(null);
        for (String group : groups) {
            spec = spec.or(groupSpecification(group));
        }
        return spec;
    }

    public static Specification<PeDimFonds> fundActiveSpecification() {
        return (Specification<PeDimFonds>) (root, query, builder) -> builder.equal(root.get("flagEnCoursCreation"), true);
    }

    public static Specification<PeDimFonds> groupSpecification(String group) {
        return (Specification<PeDimFonds>) (root, query, builder) -> {
            Join<PeDimFonds, PeDimGrpFonds> type = root.join("peDimGrpFonds");
            return builder.like(builder.lower(type.get(
                    "grpFondsId")), group.toLowerCase().trim()
                            .toLowerCase() + "%");
        };
    }
}
