package com.saintgobain.dsi.website4sg.core.web.rest.limited.incapsula;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.incapsula.bean.AclsConfiguration;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatsConfiguration;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;
import com.saintgobain.dsi.starter.incapsula.service.IncapsulaResponse;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DomainService;

import io.swagger.annotations.Api;
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
public class ConfigurationEndpointLimited {

    private final DomainService domainService;

    public ConfigurationEndpointLimited(DomainService domainService) {
        this.domainService = domainService;
    }

    @PutMapping("/{domainCode}/configure/security")
    @ApiOperation(value = "Configure security in Incapsula", notes = "Configure security in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureSecurity(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Threats Configuration", required = true) @Valid @RequestBody ThreatsConfiguration conf,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureSecurity - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureSecurity(domainCode, conf, authentication));

    }

    @PutMapping("/{domainCode}/configure/acl")
    @ApiOperation(value = "Configure acl in Incapsula", notes = "Configure acl in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> configureAcl(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Acls Configuration", required = true) @Valid @RequestBody AclsConfiguration conf,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: configureAcl - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.configureAcl(domainCode, conf, authentication));

    }

    @PutMapping("/{domainCode}/certificate/{certificateCode}")
    @ApiOperation(value = "Upload the certificate to the site in Incapsula", notes = "Upload the certificate to the site in Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> uploadCertificate(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            @ApiParam(value = "Certificate Id", required = true) @PathVariable("certificateCode") String certificateCode,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: uploadCertificate - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.uploadCertificate(domainCode, certificateCode, authentication));

    }

    @DeleteMapping("/{domainCode}/certificate")
    @ApiOperation(value = "Remove the certificate of the site from Incapsula", notes = "Remove the certificate of the site from Incapsula", response = IncapsulaResponse.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation successfully done."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<IncapsulaResponse> removeCertificate(
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode,
            Authentication authentication)
            throws IncapsulaException, Website4sgCoreException {
        log.info("Action: removeCertificate - Domain Id: " + domainCode + " - User: " + authentication.getName());
        return ResponseEntity.ok(domainService.removeCertificate(domainCode, authentication));

    }

}
