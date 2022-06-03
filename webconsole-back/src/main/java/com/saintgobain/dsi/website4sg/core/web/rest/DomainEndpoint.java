package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class DomainEndpoint {

    private final DomainService domainService;

    public DomainEndpoint(DomainService domainService) {
        this.domainService = domainService;
    }

    @PutMapping("/{domainCode}")
    @ApiOperation(value = "Create Or Update the Domain", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Domain> createOrUpdateDomain(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Domain detail", required = true) @Valid @RequestBody Domain domain)
            throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {

        return domainService.createOrUpdateDomain(domainCode, domain);
    }

    @DeleteMapping("/{domainCode}")
    @ApiOperation(value = "Remove the domain", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteDomain(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode)
            throws Website4sgCoreException {

        domainService.delete(domainCode);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/{domainCode}/transfer/websites/{websiteCode}/docroots/{docrootCode}/env/{environmentCode}")
    @ApiOperation(value = "Transfer the domain to another deployed environment", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Domain> transferDomain(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode)
            throws Website4sgCoreException {

        return domainService.transferDomain(domainCode, websiteCode, docrootCode, environmentCode);
    }

    @GetMapping("/orphans")
    @ApiOperation(value = "Get all Orphans Domains", response = Domain.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<Domain>> getAllOrphanDomain(
            Pageable pageable)
            throws Website4sgCoreException {
        return ResponseEntity.ok(domainService.getAllOrphanDomain(pageable));
    }
}
