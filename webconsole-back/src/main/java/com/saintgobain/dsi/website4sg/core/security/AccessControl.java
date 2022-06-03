package com.saintgobain.dsi.website4sg.core.security;

import org.springframework.security.core.Authentication;

public interface AccessControl<T> {

    public default boolean isAdmin(final Authentication authentication, Admin admin) {
        return authentication.getAuthorities().stream().anyMatch(right -> right.getAuthority().equals(admin
                .getAdmin()));
    }

    public default boolean checkAccessRights(final String project, final String code,
            final Authentication authentication,
            final Roles... roles) {

        if (roles.length == 0) {
            return authentication.getAuthorities().stream().anyMatch(right -> right.getAuthority().startsWith(project
                    + ":" + code + ":"));

        } else {
            return authentication.getAuthorities().stream().anyMatch(right -> filterRoles(project, code, right
                    .getAuthority(), roles));
        }

    }

    public default boolean filterRoles(final String project, final String code, final String authority,
            final Roles... roles) {

        // example 1: ddc:saintgobain2v2:OWNER
        // example 2: ddc:saintgobain2v2:EXTERNAL
        // example 3: w:isover:BUSINESS
        for (Roles role : roles) {
            if (authority.equals(project + ":" + code + ":" + role.name())) {
                return true;
            }
        }

        return false;
    }

    public boolean isAuthorized(T resource, Authentication authentication, Roles... roles);

}
