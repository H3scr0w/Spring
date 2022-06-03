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

import com.saintgobain.dsi.starter.qualys.bean.webapp.WebApp;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.qualys.WebAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class WebAppEndpoint.
 */
@Api(value = "/tools/qualys/webapps")
@RequestMapping("/tools/qualys/webapps")
@RestController
public class WebAppEndpoint {

    /** The web app service. */
    private final WebAppService webAppService;

    /**
     * Instantiates a new web app endpoint.
     *
     * @param webAppService the web app service
     */
    public WebAppEndpoint(WebAppService webAppService) {
        this.webAppService = webAppService;

    }

    @PostMapping
    @ApiOperation(value = "Create a new 'WebApp'", notes = "Allows to create a new 'WebApp'", response = WebApp.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "WebApp successfully created. Return location link of the created application."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebApp> createWebApp(
            @ApiParam(value = "WebApp", required = true) @Valid @RequestBody WebApp webApp)
            throws QualysException, Website4sgCoreException,
            URISyntaxException {

        WebApp result = webAppService.create(webApp);

        return ResponseEntity.created(new URI("/api/tools/qualys/webapps/" + result.getId()))
                .body(result);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read a 'WebApp'", notes = "Allows to read a 'WebApp'", response = WebApp.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebApp successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebApp> readWebApp(
            @ApiParam(value = "WebApp Id", required = true) @PathVariable("id") Long id)
            throws QualysException, Website4sgCoreException {

        return ResponseEntity.ok(webAppService.read(id));

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a 'WebApp'", notes = "Allows to update a 'WebApp'", response = WebApp.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebApp successfully updated."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebApp> updateWebApp(
            @ApiParam(value = "WebApp Id", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "WebApp", required = true) @Valid @RequestBody WebApp webApp)
            throws QualysException, Website4sgCoreException {

        return ResponseEntity.ok(webAppService.update(id, webApp));

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a 'WebApp'", notes = "Allows to delete a 'WebApp'", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebApp successfully deleted."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteWebApp(
            @ApiParam(value = "WebApp Id", required = true) @PathVariable("id") Long id)
            throws QualysException, Website4sgCoreException {

        webAppService.delete(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    @ApiOperation(value = "Get all 'WebApp'", notes = "Allows to get all 'WebApp'", response = WebApp.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebApps successfully returned."),
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
    public ResponseEntity<Page<WebApp>> getWebApps(
            Pageable pageable)
            throws QualysException {

        return ResponseEntity.ok(webAppService.getAll(pageable));

    }

    @PostMapping("/search")
    @ApiOperation(value = "Search among all 'WebApp'", notes = "Allows to search among all 'WebApp'", response = WebApp.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "WebApps successfully returned."),
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
    public ResponseEntity<Page<WebApp>> getWebApps(
            Pageable pageable, @ApiParam(value = "Filters", required = true) @NotNull @RequestBody Filters filters)
            throws QualysException {

        return ResponseEntity.ok(webAppService.search(pageable, filters));

    }

}
