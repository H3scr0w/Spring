package com.saintgobain.dsi.website4sg.core.web.rest.limited.incapsula;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
public class SiteEndpointLimited {

    private final DomainService domainService;

    public SiteEndpointLimited(DomainService domainService) {
        this.domainService = domainService;
    }

    @PostMapping("/status/{domainCode}")
    @ApiOperation(value = "Get site status from Incapsula", notes = "Get site status from Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> getStatus(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Site", required = false) @RequestBody Site site, Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: getStatus - Domaine Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.getStatus(domainCode, site, authentication));

    }

    @PutMapping("/{domainCode}/configure/site")
    @ApiOperation(value = "Configure origin server in Incapsula", notes = "Configure origin server in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureSite(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Site", required = true) @NotNull @RequestBody Site site, Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureSite - Domaine Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureSite(domainCode, site, authentication));

    }

}
