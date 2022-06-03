package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import java.util.List;

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
import com.saintgobain.dsi.website4sg.core.service.DrupalDocrootCoreService;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreBody;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreHeader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class DrupalDocrootCoreEndpointLimited.
 */
@Api(value = "/v1/drupaldocrootcore")
@RequestMapping("/v1/drupaldocrootcore")
@RestController
public class DrupalDocrootCoreEndpointLimited {

    /** The drupal docroot core service. */
    private DrupalDocrootCoreService drupalDocrootCoreService;

    /**
     * Instantiates a new drupal docroot core endpoint limited.
     *
     * @param drupalDocrootCoreService the drupal docroot core service
     */
    public DrupalDocrootCoreEndpointLimited(DrupalDocrootCoreService drupalDocrootCoreService) {
        this.drupalDocrootCoreService = drupalDocrootCoreService;
    }

    /**
     * Gets the all drupal docroot core.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all drupal docroot core
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all DrupalDocrootCore", response = DrupalDocrootCoreHeader.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<DrupalDocrootCoreBody>> getAllDrupalDocrootCore(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The name of drupal") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(drupalDocrootCoreService.getAllDrupalDocrootCoreBody(pageable, name,
                authentication, search));
    }

    /**
     * Gets the drupal docroot core detail.
     *
     * @param drupaldocrootcoreCode the drupaldocrootcore code
     * @param authentication the authentication
     * @return the drupal docroot core detail
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{drupaldocrootCoreCode}")
    @ApiOperation(value = "Get the DrupalDocrootCore detail", notes = "", response = DrupalDocrootCoreBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<DrupalDocrootCoreBody> getDrupalDocrootCoreDetail(
            @ApiParam(value = "DrupalDocrootCore Id", required = true) @PathVariable("drupaldocrootCoreCode") String drupaldocrootcoreCode,
            Authentication authentication)
            throws Website4sgCoreException {

        return ResponseEntity.ok(drupalDocrootCoreService.getDrupalDocrootCoreBody(drupaldocrootcoreCode,
                authentication));

    }

    /**
     * Gets the drupal docroot core docroots.
     *
     * @param pageable the pageable
     * @param drupaldocrootCoreCode the drupaldocroot core code
     * @param authentication the authentication
     * @return the drupal docroot core docroots
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{drupaldocrootCoreCode}/docroots")
    @ApiOperation(value = "Get the docroot list using the specified DrupalDocrootCore", notes = "By default, fetchLimit parameter is set to 25", response = DocrootHeader.class, responseContainer = "List", authorizations = {
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
    public ResponseEntity<List<DocrootHeader>> getDrupalDocrootCoreDocroots(
            Pageable pageable,
            @ApiParam(value = "DrupalDocrootCode Id", required = true) @PathVariable("drupaldocrootCoreCode") String drupaldocrootCoreCode,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(drupalDocrootCoreService.getAllDocrootsByDrupalCode(pageable,
                drupaldocrootCoreCode,
                authentication));
    }

}
