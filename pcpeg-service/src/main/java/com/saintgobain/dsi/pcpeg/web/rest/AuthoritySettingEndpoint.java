package com.saintgobain.dsi.pcpeg.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;
import com.saintgobain.dsi.pcpeg.dto.AuthoritySettingDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.AuthoritySettingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/authority-settings")
@RequestMapping("/authority-settings")
@RestController
@RequiredArgsConstructor
public class AuthoritySettingEndpoint {
    private final AuthoritySettingService authoritySettingService;


    @GetMapping(path = "/companies/{societeSid}")
    @ApiOperation(value = "Get all authority settings", response = AuthoritySettingDTO.class, authorizations = {
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
            + "@pcpegPropertiesResolver.users,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<Page<AuthoritySettingDTO>> getAllAuthoritySettings(Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            Pageable pageable) throws PcpegException {
        return ResponseEntity.ok(authoritySettingService.getAllAuthoritySettings(authentication, societeSid, pageable));
    }


    @PostMapping(path = "/companies/{societeSid}")
    @ApiOperation(value = "create or Update PeParHabilitations", response = PeParHabilitations.class, authorizations = {
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
            + "@pcpegPropertiesResolver.users,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<AuthoritySettingDTO> createOrUpdateAuthoritySetting(Authentication authentication,
            @Valid @RequestBody(required = true) AuthoritySettingDTO authoritySettingDTO,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid)
            throws PcpegException, URISyntaxException {
        AuthoritySettingDTO savedPeParHabilitations = authoritySettingService.createOrUpdateAuthoritySetting(
                authentication, authoritySettingDTO, societeSid);
        return ResponseEntity.created(new URI("/companies/" + societeSid)).body(
                savedPeParHabilitations);
    }

    @DeleteMapping("/{authorityId}/companies/{societeSid}")
    @ApiOperation(value = "Delete the authority setting", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.users,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<Void> deleteAuthoritySetting(
            Authentication authentication,
            @ApiParam(value = "authorityId", required = true) @PathVariable(value = "authorityId", required = true) Short authorityId,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid)
            throws PcpegException {
        authoritySettingService.deleteAuthoritySetting(authentication, authorityId, societeSid);
        return ResponseEntity.ok().build();
    }
}
