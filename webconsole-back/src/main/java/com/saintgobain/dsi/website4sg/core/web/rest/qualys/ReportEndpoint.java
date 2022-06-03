package com.saintgobain.dsi.website4sg.core.web.rest.qualys;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.qualys.bean.report.Report;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.qualys.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class ReportEndpoint.
 */
@Api(value = "/tools/qualys/reports")
@RequestMapping("/tools/qualys/reports")
@RestController
public class ReportEndpoint {

    /** The report service. */
    private final ReportService reportService;

    /**
     * Instantiates a new report endpoint.
     *
     * @param reportService the report service
     */
    public ReportEndpoint(ReportService reportService) {
        this.reportService = reportService;

    }

    @PostMapping
    @ApiOperation(value = "Create a new 'Report'", notes = "Allows to create a new 'Report'", response = Report.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "WebApp successfully created. Return location link of the created application."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Report> createReport(
            @ApiParam(value = "Report", required = true) @Valid @RequestBody Report report)
            throws QualysException, Website4sgCoreException,
            URISyntaxException {

        Report result = reportService.create(report);

        return ResponseEntity.created(new URI("/api/tools/qualys/reports/" + result.getId()))
                .body(result);

    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Read a 'Report'", notes = "Allows to read a 'Report'", response = Report.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Report> readReport(
            @ApiParam(value = "Report Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        return ResponseEntity.ok(reportService.read(id));

    }

    @GetMapping("/status/{id}")
    @ApiOperation(value = "Read the status of a 'Report'", notes = "Allows to read the status of a 'Report'", response = Report.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report Status successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Report> readReportStatus(
            @ApiParam(value = "Report Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        return ResponseEntity.ok(reportService.readStatus(id));

    }

    @PostMapping("/download/{id}")
    @ApiOperation(value = "Download a 'Report'", notes = "Allows to download a 'Report'", response = Byte.class, responseContainer = "List", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report successfully downloaded."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<ByteArrayResource> downloadReport(
            @ApiParam(value = "Report Id", required = true) @PathVariable("id") Long id) {

        ByteArrayResource report = reportService.download(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=Report_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a 'Report'", notes = "Allows to delete a 'Report'", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report successfully deleted."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteReport(
            @ApiParam(value = "Report Id", required = true) @PathVariable("id") Long id)
            throws QualysException {

        reportService.delete(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    @ApiOperation(value = "Get all 'Report'", notes = "Allows to get all 'Report'", response = Report.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reports successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<Report>> getReports(
            Pageable pageable)
            throws QualysException {

        return ResponseEntity.ok(reportService.getAll(pageable));

    }

    @PostMapping("/search")
    @ApiOperation(value = "Search among all 'Report'", notes = "Allows to search among all 'Report'", response = Report.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reports successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<Report>> getReports(
            Pageable pageable, @ApiParam(value = "Filters", required = true) @NotNull @RequestBody Filters filters)
            throws QualysException {

        return ResponseEntity.ok(reportService.search(pageable, filters));

    }

}
