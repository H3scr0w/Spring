package com.saintgobain.dsi.website4sg.core.web.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.AccessRightService;
import com.saintgobain.dsi.website4sg.core.web.bean.AccessRightBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/accessrights")
@RequestMapping("/v1/accessrights")
@RestController
public class AccessRightEndpoint {

    private AccessRightService accessRightService;

    public AccessRightEndpoint(AccessRightService accessRightsService) {
        this.accessRightService = accessRightsService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Access rights managed by Website4sg", notes = "By default, fetchLimit parameter is set to 25", response = AccessRightBody.class, responseContainer = "List", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<AccessRightBody>> getAllAccessRightBody(
            Pageable pageable,
            @ApiParam(value = "The user email") @RequestParam(value = "email", required = false) String email)
            throws Website4sgCoreException {

        return ResponseEntity.ok(accessRightService.getAllAccessRights(pageable, email));
    }

    @GetMapping("/{accessRightId}")
    @ApiOperation(value = "Get the AccessRight", notes = "", response = AccessRightBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<AccessRightBody> getAccessRightBody(
            @ApiParam(value = "AccessRight Id", required = true) @PathVariable("accessRightId") Long accessRightId)
            throws Website4sgCoreException {

        return ResponseEntity.ok(accessRightService.getAccessRightBody(accessRightId));

    }

    @PutMapping("/{accessRightId}")
    @ApiOperation(value = "Create or update an accessRight to a User for a Project with a Role", notes = "", response = AccessRightBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<AccessRightBody> createOrUpdateAccessRight(
            @ApiParam(value = "AccessRight Id", required = true) @PathVariable("accessRightId") Long accessRightId,
            @ApiParam(value = "AccessRight detail", required = true) @Valid @RequestBody AccessRightBody accessRight)
            throws Website4sgCoreException, URISyntaxException {
        return accessRightService.createOrUpdateAccessRight(accessRightId, accessRight);

    }

    @DeleteMapping("/{accessRightId}")
    @ApiOperation(value = "Remove an accessRight", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteEnvironmentDetail(
            @ApiParam(value = "AccessRight Id", required = true) @PathVariable("accessRightId") Long accessRightId)
            throws Website4sgCoreException {

        accessRightService.delete(accessRightId);
        return ResponseEntity.ok().build();

    }

}
