package com.saintgobain.dsi.website4sg.core.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

@RunWith(SpringRunner.class)
public class ProjectAccessControlTest {

    @Test
    public void filterRolesTest() {

        ProjectAccessControl projectAccessControl = new ProjectAccessControl();

        WebsiteEntity website = new WebsiteEntity();
        website.setCode("saint-gobain.com");

        ProjectEntity project = new ProjectEntity();
        project.setWebsite(website);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.addAll(Arrays.asList(new SimpleGrantedAuthority("w:joinus.saint-gobain.com:Owner"),
                new SimpleGrantedAuthority("ddc:saintgobain2v2core:LocalIT")));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "test@saint-gobain.com", null,
                grantedAuthorities);

        boolean result = projectAccessControl.isAuthorized(project, authentication, Roles.LocalIT, Roles.Business,
                Roles.External, Roles.Owner);
        Assert.assertEquals(false, result);

        result = projectAccessControl.isAuthorized(project, authentication, Roles.LocalIT, Roles.Business,
                Roles.External);
        Assert.assertEquals(false, result);

        result = projectAccessControl.isAuthorized(project, authentication);
        Assert.assertEquals(false, result);

    }

}
