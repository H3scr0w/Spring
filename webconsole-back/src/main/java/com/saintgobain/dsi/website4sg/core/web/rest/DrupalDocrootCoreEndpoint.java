package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DrupalDocrootCoreService;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCore;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class DrupalDocrootCoreEndpoint.
 */
@Api(value = "/v1/drupaldocrootcore")
@RequestMapping("/v1/drupaldocrootcore")
@RestController
public class DrupalDocrootCoreEndpoint {
    private DrupalDocrootCoreService drupalDocrootCoreService;

    /**
     * Instantiates a new drupal docroot core endpoint.
     *
     * @param drupalDocrootCoreService the drupal docroot core service
     */
    public DrupalDocrootCoreEndpoint(DrupalDocrootCoreService drupalDocrootCoreService) {
        this.drupalDocrootCoreService = drupalDocrootCoreService;
    }

    /**
     * Creates the or update drupal docroot core.
     *
     * @param drupaldocrootCoreCode the drupaldocroot core code
     * @param drupalDocrootCore the drupal docroot core
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     * @throws URISyntaxException the URI syntax exception
     */
    @PutMapping("/{drupaldocrootCoreCode}")
    @ApiOperation(value = "Create Or Update the DrupalDocrootCore", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<DrupalDocrootCoreEntity> createOrUpdateDrupalDocrootCore(
            @ApiParam(value = "DrupalDocrootCore Id", required = true) @PathVariable("drupaldocrootCoreCode") String drupaldocrootCoreCode,
            @ApiParam(value = "DrupalDocrootcore Detail", required = true) @Valid @RequestBody DrupalDocrootCore drupalDocrootCore)
            throws Website4sgCoreException, URISyntaxException, UnsupportedEncodingException {

        return drupalDocrootCoreService.createOrUpdate(drupaldocrootCoreCode, drupalDocrootCore);
    }

}
