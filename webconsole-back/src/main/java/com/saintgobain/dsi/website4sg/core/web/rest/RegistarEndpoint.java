package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.RegistarService;
import com.saintgobain.dsi.website4sg.core.web.bean.Registar;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/hosting/registars")
@RequestMapping("/v1/hosting/registars")
@RestController
public class RegistarEndpoint {

    private final RegistarService registarService;

    public RegistarEndpoint(RegistarService registarService) {
        this.registarService = registarService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Registars", response = Registar.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<Registar>> getRegistars(
            Pageable pageable,
            @ApiParam(value = "The registar name") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(registarService.getAllRegistar(pageable, name, search));
    }

    @PutMapping("/{registarCode}")
    @ApiOperation(value = "Create Or Update the Registar", response = Registar.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Registar> createOrUpdateLoadBalancer(
            @ApiParam(value = "Registar Id", required = true) @PathVariable("registarCode") String registarCode,
            @ApiParam(value = "Registar detail", required = true) @Valid @RequestBody Registar registar)
            throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {

        return registarService.createOrUpdateRegistar(registarCode, registar);
    }

}
