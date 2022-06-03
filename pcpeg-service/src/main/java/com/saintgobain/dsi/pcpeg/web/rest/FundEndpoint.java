package com.saintgobain.dsi.pcpeg.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.FundService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/funds")
@RequestMapping("/funds")
@RestController
@RequiredArgsConstructor
public class FundEndpoint {

    private final FundService fundService;

    @GetMapping
    @ApiOperation(value = "Get all funds", response = FundDTO.class, authorizations = {
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
    public ResponseEntity<Page<FundDTO>> getAllFunds(Pageable pageable,
            @ApiParam(value = "isActive", required = false) @RequestParam(value = "isActive", required = false) Boolean isActive,
            @ApiParam(value = "label", required = false) @RequestParam(value = "label", required = false) String label,
            @ApiParam(value = "groups", allowMultiple = true, type = "array", collectionFormat = "multi", example = "groups=DIV,PCL or groups=DIV&groups=PCL", required = false) @RequestParam(value = "groups", required = false) List<String> groups)
            throws PcpegException {
        return ResponseEntity.ok(fundService.getAllFunds(pageable, label, groups, isActive));
    }

    @GetMapping("/download")
    @ApiOperation(value = "Download the Funds", response = ByteArrayResource.class, authorizations = {
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
    public ResponseEntity<byte[]> dowloadFunds(
            @ApiParam(value = "label", required = false) @RequestParam(value = "label", required = false) String label,
            @ApiParam(value = "groups", allowMultiple = true, type = "array", collectionFormat = "multi", example = "groups=DIV,PCL or groups=DIV&groups=PCL", required = false) @RequestParam(value = "groups", required = false) List<String> groups)
            throws PcpegException {
        byte[] data = fundService.dowloadFunds(label, groups);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Fonds.xlxs")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(data);
    }

    @PostMapping
    @ApiOperation(value = "Create a fund", response = FundDTO.class, authorizations = {
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
    public ResponseEntity<FundDTO> createFund(
            @ApiParam(value = "fundDTO", required = true) @RequestBody(required = true) FundDTO fundDTO)
            throws URISyntaxException,
            PcpegException {
        FundDTO fund = fundService.createFund(fundDTO);
        return ResponseEntity.created(new URI("/funds/" + fund.getFundId())).body(fund);
    }

    @PutMapping(path = "/{fundId}")
    @ApiOperation(value = "Update a fund", response = FundDTO.class, authorizations = {
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
    public ResponseEntity<FundDTO> updateFund(
            @ApiParam(value = "fundId", required = true) @PathVariable(value = "fundId", required = true) Short fundId,
            @ApiParam(value = "fundDTO", required = true) @RequestBody(required = true) FundDTO fundDTO)
            throws URISyntaxException,
            PcpegException {
        return ResponseEntity.ok(fundService.updateFund(fundId, fundDTO));
    }

}
