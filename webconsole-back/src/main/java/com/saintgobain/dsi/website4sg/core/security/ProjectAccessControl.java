package com.saintgobain.dsi.website4sg.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

@Component
public class ProjectAccessControl implements AccessControl<ProjectEntity> {

    @Override
    public boolean isAuthorized(ProjectEntity resource, Authentication authentication, Roles... roles) {

        boolean isAllowed = false;

        DrupalDocrootCoreEntity ddc = resource.getDrupaldocrootcore();

        if (ddc != null) {
            String ddcCode = ddc.getCode();
            isAllowed = checkAccessRights(ProjectTypeId.D_DOCROOTCORE.getName(), ddcCode, authentication, roles);

        } else {

            WebsiteEntity website = resource.getWebsite();
            String websiteCode = website.getCode();
            isAllowed = checkAccessRights(ProjectTypeId.D_WEBSITE.getName(), websiteCode, authentication, roles);

        }

        return isAllowed;
    }

}
