package com.saintgobain.dsi.website4sg.core.web.rest.incapsula;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.incapsula.bean.Site;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;
import com.saintgobain.dsi.starter.incapsula.service.IncapsulaResponse;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DomainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Api(value = "/tools/incapsula/sites")
@RequestMapping("/tools/incapsula/sites")
@RestController
@Slf4j
public class SiteEndpoint {

    private final DomainService domainService;

    public SiteEndpoint(DomainService domainService) {
        this.domainService = domainService;
    }

    @PostMapping
    @ApiOperation(value = "Add a site in Incapsula", notes = "Add a site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<IncapsulaResponse> addWebsite(
            @ApiParam(value = "Site", required = true) @NotNull @RequestBody Site site, Authentication authentication)
            throws Website4sgCoreException, IncapsulaException {
        log.info("Action: addWebsite - Domain: " + site.getDomain() + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.addSite(site));

    }

}
