package com.saintgobain.dsi.pcpeg.web.rest;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.PeRefFormulaire;
import com.saintgobain.dsi.pcpeg.dto.EmailTemplateDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.EmailTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/email-templates")
@RequestMapping("/email-templates")
@RestController
@RequiredArgsConstructor
public class EmailTemplateEndpoint {
    private final EmailTemplateService emailTemplateService;


    @GetMapping(path = "/{formulaireId}")
    @ApiOperation(value = "Get the Email Template", response = PeRefFormulaire.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<PeRefFormulaire> getEmailTemplateById(
            @ApiParam(value = "formulaireId", required = true) @PathVariable(value = "formulaireId", required = true) String formulaireId)
            throws PcpegException {
        return ResponseEntity.ok(emailTemplateService.getEmailTemplateById(formulaireId));
    }


    @PutMapping(path = "/{formulaireId}")
    @ApiOperation(value = "Update a Email Template", response = PeRefFormulaire.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<PeRefFormulaire> updateEmailTemplate(
            Authentication authentication,
            @ApiParam(value = "formulaireId", required = true) @PathVariable(value = "formulaireId", required = true) String formulaireId,
            @Valid @RequestBody(required = true) EmailTemplateDTO emailTemplateDTO)
            throws PcpegException {
        return ResponseEntity.ok(emailTemplateService.updateEmailTemplate(formulaireId, emailTemplateDTO));
    }
}
