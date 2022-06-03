package com.saintgobain.dsi.pcpeg.web.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.YearService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/years")
@RequestMapping("/years")
@RestController
@RequiredArgsConstructor
public class YearEndpoint {

    private final YearService yearService;


    @GetMapping
    @ApiOperation(value = "Get years", response = PeDimAnnee.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin,"
            + "@pcpegPropertiesResolver.users)")
    public ResponseEntity<List<PeDimAnnee>> getYears() throws PcpegException {
		return ResponseEntity.ok(yearService.getYears());
    }

}
