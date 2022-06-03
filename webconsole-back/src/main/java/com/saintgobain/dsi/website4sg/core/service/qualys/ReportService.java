package com.saintgobain.dsi.website4sg.core.service.qualys;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.bean.report.Report;
import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScan;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Criteria;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.service.RestService;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.ScanTypeId;
import com.saintgobain.dsi.website4sg.core.domain.scan.ReportEntity;
import com.saintgobain.dsi.website4sg.core.domain.scan.ScanEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.ReportRepository;
import com.saintgobain.dsi.website4sg.core.repository.ScanRepository;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;

@Service
@Transactional
public class ReportService extends RestService<Report> {

    private final ScanRepository scanRepository;

    private final ReportRepository reportRepository;

    public ReportService(RestTemplate qualysRestTemplate, ScanRepository scanRepository,
            ReportRepository reportRepository) {
        super(qualysRestTemplate);
        this.scanRepository = scanRepository;
        this.reportRepository = reportRepository;
    }

    public Report create(Report report) throws QualysException, Website4sgCoreException {

        WasScan scan = report.getConfig().getScanReport().getTarget().getScans().getScan();

        if (scan.getId() == null) {
            throw new BadRequestException(ErrorCodes.REPORT_BAD_CONFIG.name());
        }

        ScanEntity scanEntity = scanRepository.findByScanToolId(scan.getId().toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        Report result = create(report, "/create/was/report");

        scanEntity.setReportToolId(result.getId().toString());

        scanRepository.save(scanEntity);

        return result;
    }

    public Report read(Long id) throws QualysException {
        return read(id, "/get/was/report/");
    }

    public Report readStatus(Long id) throws QualysException {
        return read(id, "/status/was/report/");
    }

    public ByteArrayResource download(Long id) {

        // Get ScanEntity by reportId (id) if found
        ScanEntity scan = scanRepository.findByReportToolId(id.toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        // Get report by this ScanEntity
        if (scan.getReport() == null || scan.getReport().getFile() == null) {

            ResponseEntity<byte[]> response = qualysRestTemplate.exchange(
                    "/download/was/report/" + id,
                    HttpMethod.GET, null, byte[].class);

            ReportEntity report = ReportEntity.builder().file(response.getBody()).scan(scan).created(new Date())
                    .build();
            scan.setReport(reportRepository.save(report));
            scanRepository.save(scan);
        }

        return new ByteArrayResource(scan.getReport().getFile());

    }

    public void delete(Long id) throws QualysException {
        // Get ScanEntity by reportId (id) if found
        ScanEntity scan = scanRepository.findByReportToolId(id.toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        delete(id, "/delete/was/report/");

        scan.setReportToolId(null);
        scanRepository.save(scan);
    }

    public Page<Report> getAll(Pageable pageable) throws QualysException {

        Page<ScanEntity> scansWsip = scanRepository.findAllByReportToolIdIsNotNullAndScanTypeIdEquals(ScanTypeId.QUALYS
                .name(),
                pageable);

        String scanReportIds = StringUtils.EMPTY;
        scanReportIds = scansWsip.getContent().stream().map(scan -> scan.getReportToolId()).collect(
                Collectors.toList()).stream().map(id -> id.toString()).collect(Collectors.joining(","));

        Criteria scanReportIdsCriteria = new Criteria("id", "IN", scanReportIds);

        Filters filters = Filters.builder().criteria(Arrays.asList(scanReportIdsCriteria)).build();

        Page<Report> reports = search(pageable, filters, "/search/was/report");

        Page<Report> reportsFiltered = new PageImpl<Report>(reports.getContent(), pageable, scansWsip
                .getTotalElements());

        return reportsFiltered;
    }

    public Page<Report> search(Pageable pageable, Filters filters) throws QualysException {
        Page<Report> reports = search(pageable, filters, "/search/was/report");

        List<Report> reportList = reports.getContent().stream().filter(report -> scanRepository.findByReportToolId(
                report.getId().toString()).isPresent()).collect(Collectors.toList());

        Integer numberFiltered = reports.getContent().size() - reportList.size();

        Long countFiltered = reports.getTotalElements() - numberFiltered;

        Page<Report> reportsFiltered = new PageImpl<Report>(reportList, pageable, countFiltered);

        return reportsFiltered;
    }

}
