package com.saintgobain.dsi.pcpeg.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.TenantAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/tenant-accounts")
@RequestMapping("/tenant-accounts")
@RestController
@RequiredArgsConstructor
public class TenantAccountEndpoint {

    private final TenantAccountService tenantAccountService;

    @GetMapping
    @ApiOperation(value = "Get all tenant accounts", response = PeRefTeneurCompte.class, authorizations = {
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
                    "Default sort order is ascending. " + "Multiple sort criteria are supported.") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin,"
            + "@pcpegPropertiesResolver.users)")
    public ResponseEntity<Page<PeRefTeneurCompte>> getAllTenantAccounts(Pageable pageable,
            @ApiParam(value = "label", required = false) @RequestParam(value = "label", required = false) String label)
            throws PcpegException {
        return ResponseEntity.ok(tenantAccountService.getAllTenantAccounts(pageable, label));
    }

}
