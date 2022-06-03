package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DocrootService;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;

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
public class DocrootEndpoint {

    private DocrootService docRootService;

    public DocrootEndpoint(DocrootService docRootService) {
        this.docRootService = docRootService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Docroots", response = DocrootHeader.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<DocrootHeader>> getAllDocroots(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The name of docroot") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(docRootService.getAllDocRootHeader(pageable, name, authentication, search));
    }

    @GetMapping("/{docrootCode}")
    @ApiOperation(value = "Get the Docroot detail", notes = "", response = DocrootHeader.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<DocrootHeader> getDocrootDetail(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(docRootService.getDocrootDetail(docrootCode, authentication));

    }

    @PutMapping("/{docrootCode}")
    @ApiOperation(value = "Create Or Update the Docroot", notes = "", response = DocrootHeader.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created or updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<DocrootHeader> createOrUpdateDocroot(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Docroot detail", required = true) @Valid @RequestBody DocrootHeader docroot,
            Authentication authentication) throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {

        Optional<DocrootEntity> docrootExist = docRootService.selectByCode(docrootCode);

        if (docrootExist.isPresent()) {

            return ResponseEntity.ok(docRootService.update(docrootExist.get(), docroot, authentication));
        } else {

            DocrootHeader docrootCreated = docRootService.create(docroot, authentication);
            return ResponseEntity.created(new URI("/api/v1/hosting/docroots/" + URLEncoder.encode(docrootCreated
                    .getCode(), StandardCharsets.UTF_8.name()))).body(
                            docrootCreated);
        }
    }

}
