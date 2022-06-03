package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.CertificateService;
import com.saintgobain.dsi.website4sg.core.web.bean.Certificate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/hosting/certificates")
@RequestMapping("/v1/hosting/certificates")
@RestController
public class CertificateEndpoint {

    private final CertificateService certificateService;

    public CertificateEndpoint(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ApiOperation(value = "Get all Certificates", response = Certificate.class, responseContainer = "Page", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<Certificate>> getCertificates(
            Pageable pageable,
            @ApiParam(value = "The certificate name") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(certificateService.getAllCertificates(name, pageable, search));
    }

    @PostMapping(value = "/{certificateCode}", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "Create Or Update the Certificate", response = Certificate.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Certificate> createOrUpdateCertificate(
            @ApiParam(value = "Certificate Id", required = true) @PathVariable("certificateCode") String certificateCode,
            @ApiParam(value = "Certificate detail", required = true) @RequestPart(value = "certParam", required = true) String certParam,
            @ApiParam(value = "Certificate File", required = false) @RequestPart(value = "certFile", required = false) MultipartFile certFile,
            @ApiParam(value = "Private Key File", required = false) @RequestPart(value = "keyFile", required = false) MultipartFile keyFile,
            Authentication authentication)
            throws Website4sgCoreException, URISyntaxException,
            IOException {

        return certificateService.createOrUpdateCertificate(certificateCode, certParam, authentication, certFile,
                keyFile);
    }

    @DeleteMapping("/{certificateCode}")
    @ApiOperation(value = "Delete the Certificate", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteCertificate(
            @ApiParam(value = "Certificate Id", required = true) @PathVariable("certificateCode") String certificateCode)
            throws Website4sgCoreException {

        certificateService.deleteCertificate(certificateCode);

        return ResponseEntity.ok().build();
    }

}
