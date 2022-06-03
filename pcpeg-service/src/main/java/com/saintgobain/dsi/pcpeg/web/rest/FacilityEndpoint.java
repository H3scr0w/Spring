package com.saintgobain.dsi.pcpeg.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.FacilityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/facilities")
@RequestMapping("/facilities")
@RestController
@RequiredArgsConstructor
public class FacilityEndpoint {
    private final FacilityService facilityService;

    @GetMapping
    @ApiOperation(value = "Get all Facilities", response = PeDimEtablissement.class, authorizations = {
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
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<Page<FacilityDTO>> getAllFacilities(Pageable pageable,
            @ApiParam(value = "label", required = false) @RequestParam(value = "label", required = false) String label,
            @ApiParam(value = "isActive", required = false) @RequestParam(value = "isActive", required = false) Boolean isActive,
            @ApiParam(value = "societeSid", required = false) @RequestParam(value = "societeSid", required = false) Integer societeSid,
            @ApiParam(value = "codeSif", required = false) @RequestParam(value = "codeSif", required = false) String codeSif) {
        return ResponseEntity.ok(facilityService.getAllFacilities(pageable, label, isActive, societeSid, codeSif));
    }

    @GetMapping("/download")
    @ApiOperation(value = "Download the Facilities", response = ByteArrayResource.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Attachment Successfully Downloaded"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<byte[]> downloadFacilities(
            @ApiParam(value = "label", required = false) @RequestParam(value = "label", required = false) String label,
            @ApiParam(value = "isActive", required = false) @RequestParam(value = "isActive", required = false) Boolean isActive,
            @ApiParam(value = "societeSid", required = false) @RequestParam(value = "societeSid", required = false) Integer societeSid,
            @ApiParam(value = "codeSif", required = false) @RequestParam(value = "codeSif", required = false) String codeSif) {
        byte[] data = facilityService.downloadFacilities(label, isActive, societeSid, codeSif);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Facilities.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(data);
    }

    @PostMapping
    @ApiOperation(value = "Create or Update a Facility", response = PeDimEtablissement.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<FacilityDTO> createOrUpdateFacility(
            @ApiParam(value = "FacilityDTO", required = true) @RequestBody(required = true) @Valid FacilityDTO facilityDTO)
            throws URISyntaxException,
            PcpegException {
        PeDimEtablissement savedFacility = facilityService.createOrUpdateFacility(facilityDTO);
        return ResponseEntity.created(new URI("/facilities/" + savedFacility.getFacilityShortLabel())).body(facilityDTO);
    }

    @GetMapping("/companies")
    @ApiOperation(value = "Get all distinct companies", response = String.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<List<FacilityDTO>> getDistinctCompanies(
            Pageable pageable,
            @ApiParam(value = "codeSif", required = false) @RequestParam(value = "codeSif", required = false) String codeSif,
            @ApiParam(value = "companyLabel", required = false) @RequestParam(value = "companyLabel", required = false) String companyLabel
    ) {
        return ResponseEntity.ok(facilityService.getDistinctCompanies(pageable, codeSif, companyLabel));
    }
}
