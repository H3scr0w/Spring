package com.saintgobain.dsi.website4sg.core.specification;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public class ProjectSpecification {

    public static Specification<ProjectEntity> joinWithUser(String userEmail) {
        return new Specification<ProjectEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<ProjectEntity, AccessRightEntity> rights = root.join("accessrightByProject");
                Join<AccessRightEntity, UsersEntity> users = rights.join("users");
                return cb.equal(users.get("email"), userEmail);

            }
        };
    }

    public static Specification<ProjectEntity> websiteIsNotNull() {
        return new Specification<ProjectEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> cq, CriteriaBuilder builder) {
                return root.get("website").isNotNull();
            }
        };
    }

    public static Specification<ProjectEntity> drupalIsNotNull() {

        return new Specification<ProjectEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> cq, CriteriaBuilder builder) {
                return root.get("drupaldocrootcore").isNotNull();
            }
        };
    }

    public static Specification<ProjectEntity> searchTextInWebsiteAllColumns(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return new Specification<ProjectEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> cq, CriteriaBuilder builder) {
                cq.distinct(true);

                return builder.or(Arrays.stream(WebsiteEntity.class.getDeclaredFields()).filter(f -> isProjectFieldOk(
                        f)).map(f -> builder.like(builder.lower(root.get("website").get(f.getName())), finalText))
                        .toArray(
                                Predicate[]::new));

            }
        };
    }

    public static Specification<ProjectEntity> searchTextInDrupalAllColumns(String text) {

        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = text;

        return new Specification<ProjectEntity>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProjectEntity> root, CriteriaQuery<?> cq, CriteriaBuilder builder) {
                cq.distinct(true);
                return builder.or(Arrays.stream(DrupalDocrootCoreEntity.class.getDeclaredFields()).filter(
                        f -> isProjectFieldOk(f)).map(f -> builder.like(builder.lower(root.get("drupaldocrootcore").get(
                                f.getName())),
                                finalText)).toArray(Predicate[]::new));

            }
        };
    }

    private static boolean isProjectFieldOk(Field f) {

        if (f.getType().getSimpleName().equalsIgnoreCase("string") && (f.getName().equalsIgnoreCase(
                "code") || f.getName().equalsIgnoreCase("name"))) {
            return true;
        } else {
            return false;
        }
    }
}
