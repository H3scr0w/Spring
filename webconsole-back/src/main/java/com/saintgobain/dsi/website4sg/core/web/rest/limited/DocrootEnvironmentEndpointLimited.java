package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DocrootEnvironmentService;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/hosting/docroots")
@RequestMapping("/v1/hosting/docroots")
@RestController
public class DocrootEnvironmentEndpointLimited {

    private DocrootEnvironmentService docrootEnvironmentService;

    public DocrootEnvironmentEndpointLimited(DocrootEnvironmentService docrootEnvironmentService) {
        this.docrootEnvironmentService = docrootEnvironmentService;
    }

    @GetMapping("/{docrootCode}/env")
    @ApiOperation(value = "Get all environment available for the specified docroot", response = DocrootEnvironmentHeader.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<DocrootEnvironmentHeader>> getAllEnvironments(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "Docroot code", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "The search engine", required = false) @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.getAllDocrootEnvironmentHeader(docrootCode, pageable,
                authentication, search));
    }

    @PostMapping("/{docrootCode}/env/{environmentCode}/domains/{domainCode}/clear-acquia-varnish")
    @ApiOperation(value = "Clear Acquia varnish cache of a domain on the specified docroot / environment", notes = "", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Void> clearAcquiaVarnish(
            Authentication authentication,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode)
            throws Website4sgCoreException {

        docrootEnvironmentService.clearAcquiaVarnish(authentication, docrootCode, environmentCode, domainCode);
        return ResponseEntity.ok().build();
    }

}
