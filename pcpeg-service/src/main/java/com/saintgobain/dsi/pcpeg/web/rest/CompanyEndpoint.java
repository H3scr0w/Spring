package com.saintgobain.dsi.pcpeg.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParAccords;
import com.saintgobain.dsi.pcpeg.dto.CompanySettingDTO;
import com.saintgobain.dsi.pcpeg.dto.CompanySettingValidationDTO;
import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.dto.PeDimSocieteDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.CompanyService;
import com.saintgobain.dsi.pcpeg.service.CompanySettingService;
import com.saintgobain.dsi.pcpeg.service.DocumentService;
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

@Api(value = "/companies")
@RequestMapping("/companies")
@RestController
@RequiredArgsConstructor
public class CompanyEndpoint {
    private final CompanyService companyService;
    private final CompanySettingService companySettingService;
    private final FundService fundService;
    private final DocumentService documentService;


    @GetMapping
    @ApiOperation(value = "Get all companies", response = PeDimSocieteDTO.class, authorizations = {
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
    public ResponseEntity<Page<PeDimSocieteDTO>> getAllCompanies(Authentication authentication, Pageable pageable)
            throws PcpegException {
        return ResponseEntity.ok(companyService.getAllCompanies(authentication, pageable));
    }

    @GetMapping(path = "/adherents-count")
    @ApiOperation(value = "Get all flagged adherent companies", response = Integer.class, authorizations = {
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
    public ResponseEntity<Integer> getFlagAdherentCompanies()
            throws PcpegException {
        return ResponseEntity.ok(companyService.getAllFlagAdherentCompanies());
    }


    @GetMapping(path = "/{societeSid}")
    @ApiOperation(value = "Get the Company", response = PeDimSocieteDTO.class, authorizations = {
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
    public ResponseEntity<PeDimSocieteDTO> getCompanyById(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid)
            throws PcpegException {
        return ResponseEntity.ok(companyService.getCompanyById(authentication, societeSid));
    }

    @PostMapping
    @ApiOperation(value = "Create a company", response = PeDimSociete.class, authorizations = {
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
    public ResponseEntity<PeDimSociete> createCompany(
            @ApiParam(value = "peDimSocieteDTO", required = true) @RequestBody(required = true) PeDimSocieteDTO peDimSocieteDTO)
            throws URISyntaxException,
            PcpegException {
        PeDimSociete savedCompany = companyService.createCompany(peDimSocieteDTO);
        return ResponseEntity.created(new URI("/companies/" + savedCompany.getSocieteSid())).body(savedCompany);
    }

    @PutMapping(path = "/{societeSid}")
    @ApiOperation(value = "Update a company", response = PeDimSocieteDTO.class, authorizations = {
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
    public ResponseEntity<PeDimSociete> updateCompany(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "company", required = true) @RequestBody(required = true) PeDimSocieteDTO peDimSocieteDTO)
            throws PcpegException,
            IOException,
            MessagingException {
        return ResponseEntity.ok(companyService.updateCompany(authentication, societeSid, peDimSocieteDTO));
    }


    @DeleteMapping("/{societeSid}")
    @ApiOperation(value = "Delete the company", response = Void.class, authorizations = {
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
    public ResponseEntity<Void> deleteCompany(
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid)
            throws PcpegException {
        companyService.deleteCompany(societeSid);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{societeSid}/comment")
    @ApiOperation(value = "Update a comment", response = PeDimSocieteDTO.class, authorizations = {
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
    public ResponseEntity<PeDimSociete> addComment(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "comment", required = false) @RequestBody(required = false) String comments)
            throws PcpegException,
            IOException,
            MessagingException {
        return ResponseEntity.ok(companyService.addComment(authentication, societeSid, comments));
    }


    @GetMapping("/download")
    @ApiOperation(value = "Download the Companies", response = ByteArrayResource.class, authorizations = {
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
    public ResponseEntity<byte[]> downloadCompanies() throws PcpegException {
        byte[] data = companyService.downloadCompanies();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Companies.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(data);
    }

    @GetMapping(path = "/settings/{societeSid}/paymentTypes/{paymentTypeId}")
    @ApiOperation(value = "Get the Company Setting for payment type", response = CompanySettingDTO.class, authorizations = {
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
    public ResponseEntity<CompanySettingDTO> getCompanySettingsByIdAndPaymentType(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "paymentTypeId", required = true) @PathVariable(value = "paymentTypeId", required = true) Integer paymentTypeId)
            throws PcpegException {
        return ResponseEntity.ok(companySettingService.getCompanySettingsByIdAndPaymentType(authentication, societeSid,
                paymentTypeId));
    }

    @PostMapping(path = "/settings/{societeSid}/paymentTypes/{paymentTypeId}")
    @ApiOperation(value = "Create or Update the Company Setting for payment type", response = Void.class, authorizations = {
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
    public ResponseEntity<CompanySettingDTO> validateCompanySettingsByIdAndPaymentType(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "paymentTypeId", required = true) @PathVariable(value = "paymentTypeId", required = true) Integer paymentTypeId,
            @RequestPart(value = "companySettingValidationDTO", required = true) CompanySettingValidationDTO companySettingValidationDTO,
            @RequestPart(value = "file", required = false) MultipartFile[] files)
            throws PcpegException, IOException {
        companySettingService.validateCompanySettingsByIdAndPaymentType(authentication,
                societeSid,
                paymentTypeId, companySettingValidationDTO, files);
        return ResponseEntity.ok(companySettingService.getCompanySettingsByIdAndPaymentType(authentication, societeSid,
                paymentTypeId));
    }

    @PostMapping(path = "/settings/{societeSid}/validate")
    @ApiOperation(value = "Validate all the Company Settings", response = Void.class, authorizations = {
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
    public ResponseEntity<Void> validateCompanySettings(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid)
            throws PcpegException, IOException {
        companySettingService.validateCompanySettings(authentication, societeSid);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/settings/{societeSid}/paymentTypes/{paymentTypeId}/fund")
    @ApiOperation(value = "Create or update fund in settings", response = FundDTO.class, authorizations = {
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
    public ResponseEntity<FundDTO> createOrUpdateFund(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "paymentTypeId", required = true) @PathVariable(value = "paymentTypeId", required = true) Integer paymentTypeId,
            @ApiParam(value = "fundDTO", required = true) @RequestBody(required = true) @Valid FundDTO fundDTO)
            throws PcpegException {
        return ResponseEntity.ok(fundService.createOrUpdateFundSettings(authentication, societeSid, paymentTypeId, fundDTO));
    }

    @GetMapping(path = "/settings/{societeSid}/documents/{documentId}/download")
    @ApiOperation(value = "Download document for a company setting", response = byte[].class, authorizations = {
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
    public ResponseEntity<byte[]> downloadDocument(
            Authentication authentication,
            @ApiParam(value = "societeSid", required = true) @PathVariable(value = "societeSid", required = true) Integer societeSid,
            @ApiParam(value = "documentId", required = true) @PathVariable(value = "documentId", required = true) Integer documentId)
            throws PcpegException, IOException {

        Pair<PeParAccords, byte[]> documentData = documentService.downloadDocument(authentication, societeSid,
                documentId);
        PeParAccords document = documentData.getKey();
        byte[] data = documentData.getValue();
        MediaType mediaType = MediaType.APPLICATION_PDF;
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getNomDocument())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(data);
    }

}
