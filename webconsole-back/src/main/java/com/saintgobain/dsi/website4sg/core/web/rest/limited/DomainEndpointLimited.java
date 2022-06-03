package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DomainService;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/hosting/domains")
@RequestMapping("/v1/hosting/domains")
@RestController
public class DomainEndpointLimited {

    private final DomainService domainService;

    public DomainEndpointLimited(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Domains", response = Domain.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Page<Domain>> getDomains(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The domain name") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The qualys enabled flag") @RequestParam(value = "qualysEnabled", required = false) Boolean qualysEnabled,
            @ApiParam(value = "The waf flag") @RequestParam(value = "waf", required = false) Boolean waf,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(domainService.getAllDomain(pageable, name, qualysEnabled, waf, authentication,
                search));
    }

    @GetMapping("/isadmin/{domainCode}")
    @ApiOperation(value = "Check user access right for specific domain", response = Boolean.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Boolean> isAdmin(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            Authentication authentication)
            throws Website4sgCoreException {
        return ResponseEntity.ok(domainService.isAdmin(domainCode, authentication));
    }

}
