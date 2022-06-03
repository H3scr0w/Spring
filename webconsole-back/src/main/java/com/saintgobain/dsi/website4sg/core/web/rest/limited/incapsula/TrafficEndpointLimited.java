package com.saintgobain.dsi.website4sg.core.web.rest.limited.incapsula;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;
import com.saintgobain.dsi.starter.incapsula.service.IncapsulaResponse;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DomainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Api(value = "/tools/incapsula/sites")
@RequestMapping("/tools/incapsula/sites")
@RestController
@Slf4j
public class TrafficEndpointLimited {

    private final DomainService domainService;

    public TrafficEndpointLimited(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping("/{domainCode}/stats/{statsId}")
    @ApiOperation(value = "Get stats of a site from Incapsula", notes = "Get stats of a site from Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
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
    public ResponseEntity<IncapsulaResponse> getStats(
            Pageable pageable,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Stats Id", allowableValues = "visits_timeseries, hits_timeseries, bandwidth_timeseries, requests_geo_dist_summary, visits_dist_summary, caching, caching_timeseries, threats, incap_rules, incap_rules_timeseries", required = true) @PathVariable("statsId") String statsId,
            @ApiParam(value = "The Time Range", defaultValue = "today", allowableValues = "today, last_7_days, last_30_days, last_90_days, month_to_date") @RequestParam(value = "timeRange", required = false) String timeRange,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: getStats - Stats: " + statsId + " - Domain Id: " + domainCode + " - User: " + authentication
                .getName());
        return ResponseEntity.ok(domainService.getStats(pageable, domainCode, statsId, timeRange, authentication));

    }

    @GetMapping("/{domainCode}/visits")
    @ApiOperation(value = "Get all visits of a site from Incapsula", notes = "Get all visits of a site from Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
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
    public ResponseEntity<IncapsulaResponse> getAllVisits(
            Pageable pageable,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "The Time Range", defaultValue = "today", allowableValues = "today, last_7_days, last_30_days, last_90_days, month_to_date") @RequestParam(value = "timeRange", required = false) String timeRange,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: getAllVisits - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.getVisits(pageable, domainCode, timeRange, authentication));

    }

}
