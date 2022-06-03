package com.saintgobain.dsi.website4sg.core.web.rest;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.EnvironmentService;
import com.saintgobain.dsi.website4sg.core.web.bean.Environment;
import com.saintgobain.dsi.website4sg.core.web.bean.EnvironmentBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class EnvironmentApiEndpoint.
 */
@RestController
@Api("/v1/environments")
@RequestMapping("/v1/environments")
public class EnvironmentApiEndpoint {

    /** The environment service. */
    private EnvironmentService environmentService;

    /**
     * Instantiates a new environment api endpoint.
     *
     * @param environmentService the environment service
     */
    public EnvironmentApiEndpoint(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    /**
     * Gets the all environments.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all environments
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all environment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<EnvironmentEntity>> getAllEnvironments(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The name of environment") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(environmentService.getAllEnvironments(pageable, name, authentication, search));
    }

    /**
     * Gets the environment.
     *
     * @param environmentCode the environment code
     * @param authentication the authentication
     * @return the environment
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{environmentCode}")
    @ApiOperation(value = "Get environment by code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<EnvironmentBody> getEnvironment(
            @ApiParam(value = "Environment code", required = true) @PathVariable("environmentCode") String environmentCode,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(environmentService.getEnvironment(environmentCode.trim(), authentication));
    }

    /**
     * Creates the or update environment.
     *
     * @param environmentCode the environment code
     * @param environment the environment
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{environmentCode}")
    @ApiOperation(value = "Update or add a environment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<EnvironmentEntity> createOrUpdateEnvironment(
            @ApiParam(value = "environment code") @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "environment object", required = true) @Valid @RequestBody Environment environment,
            Authentication authentication)
            throws Website4sgCoreException {

        return ResponseEntity.ok(environmentService.createOrUpdateEnvironment(environmentCode.trim(), environment,
                authentication));
    }

}
