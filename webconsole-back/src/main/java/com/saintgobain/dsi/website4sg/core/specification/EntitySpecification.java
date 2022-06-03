package com.saintgobain.dsi.website4sg.core.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;


public class EntitySpecification<T> {

    public static <T> Specification<T> searchTextInAllColumns(String text) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return (Specification<T>) (root, query, builder) -> {
            return builder.or(root.getModel().getDeclaredSingularAttributes().stream().filter(a -> {
                if (a.getJavaType().getSimpleName().equalsIgnoreCase("string")) {
                    return true;
                } else {
                    return false;
                }
            }).map(a -> builder.like(root.get(a.getName()), finalText)).toArray(Predicate[]::new));
        };
    }
}
