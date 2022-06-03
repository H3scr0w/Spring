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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScan;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.website4sg.core.service.qualys.WasScanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class WasScanEndpoint.
 */
@Api(value = "/tools/qualys/wasscans")
@RequestMapping("/tools/qualys/wasscans")
@RestController
public class WasScanEndpoint {

    /** The was scan service. */
    private final WasScanService wasScanService;

    /**
     * Instantiates a new was scan endpoint.
     *
     * @param wasScanService the was scan service
     */
    public WasScanEndpoint(WasScanService wasScanService) {
        this.wasScanService = wasScanService;

    }

    @PostMapping
    @ApiOperation(value = "Launch a new 'WasScan'", notes = "Allows to launch a new 'WasScan'", response = WasScan.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "WasScan successfully launched"),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WasScan> launchWasScan(
            @ApiParam(value = "WasScan", required = true) @Valid @RequestBody WasScan wasScan)
            throws QualysException, URISyntaxException {

        WasScan result = wasScanService.launch(wasScan);

        return ResponseEntity.created(new URI("/api/tools/qualys/wasscans/" + result.getId()))
                .body(result);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read a 'WasScan'", notes = "Allows to read a 'WasScan'", response = WasScan.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WasScan successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WasScan> readWasScan(
            @ApiParam(value = "WasScan Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        return ResponseEntity.ok(wasScanService.read(id));

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a 'WasScan'", notes = "Allows to delete a 'WasScan'", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WasScan successfully deleted."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteWasScan(
            @ApiParam(value = "WasScan Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        wasScanService.delete(id);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/cancel/{id}")
    @ApiOperation(value = "Cancel a 'WasScan'", notes = "Allows to cancel a 'WasScan'", response = WasScan.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WasScan successfully canceled."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WasScan> cancelWasScan(
            @ApiParam(value = "WasScan Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        return ResponseEntity.ok(wasScanService.cancel(id));
    }

    @GetMapping
    @ApiOperation(value = "Get all 'WasScan'", notes = "Allows to get all 'WasScan'", response = WasScan.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WasScans successfully returned."),
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
    public ResponseEntity<Page<WasScan>> getWasScans(
            Pageable pageable)
            throws QualysException {

        return ResponseEntity.ok(wasScanService.getAll(pageable));

    }

    @PostMapping("/search")
    @ApiOperation(value = "Search among all 'WasScan'", notes = "Allows to search among all 'WasScan'", response = WasScan.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WasScans successfully returned."),
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
    public ResponseEntity<Page<WasScan>> getWasScans(
            Pageable pageable, @ApiParam(value = "Filters", required = true) @NotNull @RequestBody Filters filters)
            throws QualysException {

        return ResponseEntity.ok(wasScanService.search(pageable, filters));

    }

}
