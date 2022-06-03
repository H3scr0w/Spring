package com.saintgobain.dsi.website4sg.core.web.rest.limited.incapsula;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.incapsula.bean.CacheMode;
import com.saintgobain.dsi.starter.incapsula.bean.CacheRules;
import com.saintgobain.dsi.starter.incapsula.bean.CacheSettings;
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
public class CacheEndpointLimited {

    private final DomainService domainService;

    public CacheEndpointLimited(DomainService domainService) {
        this.domainService = domainService;
    }

    @DeleteMapping("/{domainCode}/cache/purge")
    @ApiOperation(value = "Purge cache of a site in Incapsula", notes = "Purge cache of a site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> purgeCache(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: PurgeCache - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.purgeCache(domainCode, authentication));

    }

    @PutMapping("/{domainCode}/cache/mode")
    @ApiOperation(value = "Configure cache mode of a site in Incapsula", notes = "Configure cache mode of a site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureCacheMode(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Cache Mode", required = true) @Valid @RequestBody CacheMode cacheMode,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureCacheMode - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureCacheMode(domainCode, cacheMode, authentication));

    }

    @PutMapping("/{domainCode}/cache/rules")
    @ApiOperation(value = "Configure cache rules of a site in Incapsula", notes = "Configure cache rules of a site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureCacheRules(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Cache Rules", required = true) @Valid @RequestBody CacheRules cacheRules,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureCacheRules - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureCacheRules(domainCode, cacheRules, authentication));

    }

    @PutMapping("/{domainCode}/cache/settings")
    @ApiOperation(value = "Configure cache settings of a site in Incapsula", notes = "Configure cache settings of a site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureCacheSettings(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Cache Settings", required = true) @Valid @RequestBody CacheSettings cacheSettings,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureCacheSettings - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureCacheSettings(domainCode, cacheSettings, authentication));

    }

}
