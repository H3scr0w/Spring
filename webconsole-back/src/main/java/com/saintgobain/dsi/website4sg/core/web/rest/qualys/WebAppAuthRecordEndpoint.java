package com.saintgobain.dsi.website4sg.core.web.rest.qualys;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthRecord;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.website4sg.core.service.qualys.WebAppAuthRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class WebAppAuthRecordEndpoint.
 */
@Api(value = "/tools/qualys/webappauthrecords")
@RequestMapping("/tools/qualys/webappauthrecords")
@RestController
public class WebAppAuthRecordEndpoint {

    /** The web app auth record service. */
    private final WebAppAuthRecordService webAppAuthRecordService;

    /**
     * Instantiates a new web app auth record endpoint.
     *
     * @param webAppAuthRecordService the web app auth record service
     */
    public WebAppAuthRecordEndpoint(WebAppAuthRecordService webAppAuthRecordService) {
        this.webAppAuthRecordService = webAppAuthRecordService;

    }

    @PostMapping
    @ApiOperation(value = "Create a new 'WebAppAuthRecord'", notes = "Allows to create a new 'WebAppAuthRecord'", response = WebAppAuthRecord.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "WebAppAuthRecord successfully created. Return location link of the created application."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebAppAuthRecord> createWebAppAuthRecord(
            @ApiParam(value = "WebAppAuthRecord", required = true) @Valid @RequestBody WebAppAuthRecord webAppAuthRecord)
            throws QualysException,
            URISyntaxException {

        WebAppAuthRecord result = webAppAuthRecordService.create(webAppAuthRecord);

        return ResponseEntity.created(new URI("/api/tools/qualys/webappauthrecords/" + result.getId()))
                .body(result);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read a 'WebAppAuthRecord'", notes = "Allows to read a 'WebAppAuthRecord'", response = WebAppAuthRecord.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebAppAuthRecord successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebAppAuthRecord> readWebAppAuthRecord(
            @ApiParam(value = "WebAppAuthRecord Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        return ResponseEntity.ok(webAppAuthRecordService.read(id));

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a 'WebAppAuthRecord'", notes = "Allows to update a 'WebAppAuthRecord'", response = WebAppAuthRecord.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebAppAuthRecord successfully updated."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebAppAuthRecord> updateWebAppAuthRecord(
            @ApiParam(value = "WebAppAuthRecord Id", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "WebAppAuthRecord", required = true) @Valid @RequestBody WebAppAuthRecord webAppAuthRecord)
            throws QualysException {

        return ResponseEntity.ok(webAppAuthRecordService.update(id, webAppAuthRecord));

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a 'WebAppAuthRecord'", notes = "Allows to delete a 'WebAppAuthRecord'", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebAppAuthRecord successfully deleted."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteWebAppAuthRecord(
            @ApiParam(value = "WebAppAuthRecord Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        webAppAuthRecordService.delete(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    @ApiOperation(value = "Get all 'WebAppAuthRecord'", notes = "Allows to get all 'WebAppAuthRecord'", response = WebAppAuthRecord.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebAppAuthRecords successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<WebAppAuthRecord>> getWebAppAuthRecords(
            Pageable pageable)
            throws QualysException {

        return ResponseEntity.ok(webAppAuthRecordService.getAll(pageable));

    }

    @PostMapping("/search")
    @ApiOperation(value = "Search among all 'WebAppAuthRecord'", notes = "Allows to search among all 'WebAppAuthRecord'", response = WebAppAuthRecord.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebAppAuthRecords successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<WebAppAuthRecord>> getWebAppAuthRecords(
            Pageable pageable, @ApiParam(value = "Filters", required = true) @NotNull @RequestBody Filters filters)
            throws QualysException {

        return ResponseEntity.ok(webAppAuthRecordService.search(pageable, filters));

    }

}
