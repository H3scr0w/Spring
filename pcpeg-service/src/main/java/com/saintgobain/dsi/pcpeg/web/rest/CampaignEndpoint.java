package com.saintgobain.dsi.pcpeg.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.domain.CampaignView;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.dto.CampaignStatsDTO;
import com.saintgobain.dsi.pcpeg.dto.CorrespondantDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.service.CampaignService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/campaigns")
@RequestMapping("/campaigns")
@RestController
@RequiredArgsConstructor
public class CampaignEndpoint {
    private final CampaignService campaignService;

    @GetMapping
    @ApiOperation(value = "Get all campaigns", response = CampaignView.class, authorizations = {
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
    public ResponseEntity<Page<CampaignView>> getAllCampaigns(
            Authentication authentication,
            Pageable pageable,
            @ApiParam(value = "year", required = true) @RequestParam(value = "year", required = false) String year,
            @ApiParam(value = "statutId", required = true) @RequestParam(value = "statutId", required = false) String statutId)
            throws PcpegException {
        return ResponseEntity.ok(campaignService.getAllCampaigns(authentication, pageable, year, statutId));
    }

    @GetMapping("/stats/years/{year}")
    @ApiOperation(value = "Get campaigns stats", response = CampaignStatsDTO.class, authorizations = {
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
    public ResponseEntity<CampaignStatsDTO> getCampaignStats(
            @ApiParam(value = "year", required = true) @PathVariable(value = "year", required = true) String year)
            throws PcpegException {
        return ResponseEntity.ok(campaignService.getCampaignStats(year));
    }

    @DeleteMapping("/companies/{societeId}/years/{year}/users")
    @ApiOperation(value = "Delete the correspondant N", response = Void.class, authorizations = {
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
    public ResponseEntity<Void> deleteCorrespondant(
            @ApiParam(value = "societeId", required = true) @PathVariable(value = "societeId", required = true) String societeId,
            @ApiParam(value = "year", required = true) @PathVariable(value = "year", required = true) String year)
            throws PcpegException {
        campaignService.deleteCorrespondant(societeId, year);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/companies/{societeId}/years/{year}/users/{sgid}")
    @ApiOperation(value = "Replace the correspondant N", response = Void.class, authorizations = {
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
    public ResponseEntity<Void> replaceCorrespondant(
            @ApiParam(value = "societeId", required = true) @PathVariable(value = "societeId", required = true) String societeId,
            @ApiParam(value = "year", required = true) @PathVariable(value = "year", required = true) String year,
            @ApiParam(value = "sgid", required = true) @PathVariable(value = "sgid", required = true) String sgid,
            @ApiParam(value = "isNotified", required = false) @RequestParam(value = "isNotified", required = false) Boolean isNotified)
            throws PcpegException, MessagingException {
        campaignService.replaceCorrespondant(societeId, year, sgid, isNotified);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email")
    @ApiOperation(value = "Notify correspondants", response = Void.class, authorizations = {
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
    public ResponseEntity<Void> notifyCorrespondants(
            @NotEmpty @Valid @RequestBody(required = true) List<CorrespondantDTO> correspondantsDTO,
            @ApiParam(value = "isReminder", required = false) @RequestParam(value = "isReminder", required = false) Boolean isReminder)
            throws PcpegException, MessagingException {
        campaignService.notifyCorrespondants(correspondantsDTO, isReminder);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @ApiOperation(value = "Create a campaign", response = PeParSuiviSocietes.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<List<PeParSuiviSocietes>> createCampaign(
            @ApiParam(value = "year", required = true) @RequestParam(value = "year", required = true) String year,
            @ApiParam(value = "copyPrevious", required = false) @RequestParam(value = "copyPrevious",required = false) Boolean copyPrevious
    )
            throws URISyntaxException,
            PcpegException {
        List<PeParSuiviSocietes> savedCampaign = campaignService.createCampaign(year,copyPrevious);
        return ResponseEntity.created(new URI("/campaigns?year=" + year)).body(savedCampaign);
    }

    @GetMapping("/download")
    @ApiOperation(value = "Download campaigns excel", response = ByteArrayResource.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Attachment Successfully Downloaded"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<byte[]> downloadCampaigns(Authentication authentication,
            @ApiParam(value = "year", required = true) @RequestParam(value = "year", required = false) String year,
            @ApiParam(value = "statutId", required = true) @RequestParam(value = "statutId", required = false) String statutId)
            throws PcpegException {
        byte[] data = campaignService.downloadCampaigns(authentication, year, statutId);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Campaigns.xlxs")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(data.length)
            .body(data);
    }


    @PostMapping("/campaigns/years/{year}/open")
    @ApiOperation(value = "Open the campaign", response = PeDimAnnee.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<PeDimAnnee> openCampaign(
            @ApiParam(value = "year", required = true) @PathVariable(value = "year", required = true) String year)
            throws PcpegException {
        return ResponseEntity.ok(campaignService.openCampaign(year));
    }

    @PostMapping("/campaigns/years/{year}/close")
    @ApiOperation(value = "Close the campaign", response = PeDimAnnee.class, authorizations = { @Authorization(value = "jwt") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin)")
    public ResponseEntity<PeDimAnnee> closeCampaign(
            @ApiParam(value = "year", required = true) @PathVariable(value = "year", required = true) String year)
            throws PcpegException {
        return ResponseEntity.ok(campaignService.closeCampaign(year));
    }

}
