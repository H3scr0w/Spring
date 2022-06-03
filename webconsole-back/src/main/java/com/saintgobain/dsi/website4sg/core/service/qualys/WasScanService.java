package com.saintgobain.dsi.website4sg.core.service.qualys;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScan;
import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScanStatus;
import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScanTarget;
import com.saintgobain.dsi.starter.qualys.bean.webapp.WebApp;
import com.saintgobain.dsi.starter.qualys.exception.BadRequestException;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Criteria;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.model.ServiceResponse;
import com.saintgobain.dsi.starter.qualys.service.RestService;
import com.saintgobain.dsi.starter.qualys.service.ServiceResponseUtil;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.ScanStatusId;
import com.saintgobain.dsi.website4sg.core.domain.enumeration.ScanTypeId;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.scan.ScanEntity;
import com.saintgobain.dsi.website4sg.core.domain.scan.ScanStatusEntity;
import com.saintgobain.dsi.website4sg.core.domain.scan.ScanTypeEntity;
import com.saintgobain.dsi.website4sg.core.repository.ScanRepository;
import com.saintgobain.dsi.website4sg.core.repository.ScanStatusRepository;
import com.saintgobain.dsi.website4sg.core.repository.ScanTypeRepository;
import com.saintgobain.dsi.website4sg.core.repository.WebsiteRepository;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class WasScanService extends RestService<WasScan> {

    private final WebsiteRepository websiteRepository;

    private final ScanRepository scanRepository;

    private final ScanTypeRepository scanTypeRepository;

    private final ScanStatusRepository scanStatusRepository;

    public WasScanService(RestTemplate qualysRestTemplate, WebsiteRepository websiteRepository,
            ScanRepository scanRepository, ScanTypeRepository scanTypeRepository,
            ScanStatusRepository scanStatusRepository) {
        super(qualysRestTemplate);
        this.websiteRepository = websiteRepository;
        this.scanRepository = scanRepository;
        this.scanTypeRepository = scanTypeRepository;
        this.scanStatusRepository = scanStatusRepository;
    }

    public WasScan launch(WasScan wasScan) throws QualysException {

        WasScanTarget target = wasScan.getTarget();
        WebApp webApp = target.getWebApp();

        WebsiteEntity website = websiteRepository.findByQualysWebAppId(webApp.getId()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.WEBSITE_PROJECT_NOT_FOUND.name()));

        if (BooleanUtils.isTrue(website.getIsQualysEnable())) {
            WasScan qualysWasScan = create(wasScan, "/launch/was/wasscan");

            ScanTypeEntity scanType = scanTypeRepository.findById(ScanTypeId.QUALYS.name()).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.SCAN_TYPE_NOT_FOUND.name()));

            ScanStatusEntity scanStatus = scanStatusRepository.findById(ScanStatusId.RUNNING.name()).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.SCAN_STATUS_NOT_FOUND.name()));

            ScanEntity scan = ScanEntity.builder()
                    .project(website.getProject())
                    .scanType(scanType)
                    .scanStatus(scanStatus)
                    .scanToolId(qualysWasScan.getId().toString())
                    .created(new Date())
                    .build();

            scanRepository.save(scan);
            return qualysWasScan;
        }

        throw new BadRequestException(ErrorCodes.WEBSITE_QUALYS_NOT_ENABLED.name());
    }

    public WasScan read(Long id) throws QualysException {

        ScanEntity scan = scanRepository.findByScanToolId(id.toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        WasScan qualysWasScan = read(id, "/get/was/wasscan/");

        ScanStatusEntity scanStatus = scanStatusRepository.findById(qualysWasScan.getStatus().name()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_STATUS_NOT_FOUND.name()));

        scan.setScanStatus(scanStatus);
        scanRepository.save(scan);

        return qualysWasScan;
    }

    public void delete(Long id) throws QualysException {

        ScanEntity scan = scanRepository.findByScanToolId(id.toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        delete(id, "/delete/was/wasscan/");

        scan.setScanToolId(null);
        scanRepository.save(scan);
    }

    public WasScan cancel(Long id) throws QualysException {

        ScanEntity scan = scanRepository.findByScanToolId(id.toString()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_NOT_FOUND.name()));

        ServiceResponse<List<WasScan>> response = qualysRestTemplate.exchange("/cancel/was/wasscan/"
                + id,
                HttpMethod.POST, null, new ParameterizedTypeReference<ServiceResponse<List<WasScan>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        ScanStatusEntity scanStatus = scanStatusRepository.findById(WasScanStatus.CANCELED.name()).orElseThrow(
                () -> new EntityNotFoundException(
                        ErrorCodes.SCAN_STATUS_NOT_FOUND.name()));

        scan.setScanStatus(scanStatus);
        scanRepository.save(scan);

        List<WasScan> dataList = response.getData();

        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.get(0);
        }

        return null;
    }

    public Page<WasScan> getAll(Pageable pageable) throws QualysException {

        Page<ScanEntity> scansWsip = scanRepository.findAllByScanToolIdIsNotNullAndScanTypeIdEquals(ScanTypeId.QUALYS
                .name(),
                pageable);

        String scanIds = StringUtils.EMPTY;
        scanIds = scansWsip.getContent().stream().map(scan -> scan.getScanToolId()).collect(
                Collectors.toList()).stream().map(id -> id.toString()).collect(Collectors.joining(","));

        Criteria scanIdsCriteria = new Criteria("id", "IN", scanIds);

        Filters filters = Filters.builder().criteria(Arrays.asList(scanIdsCriteria)).build();

        Page<WasScan> scans = search(pageable, filters, "/search/was/wasscan");

        // Refresh Scans Wsip from Qualys in async
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        forkJoinPool.execute(() -> scans.getContent().parallelStream().forEach(scan -> {
            try {
                read(scan.getId());
            } catch (QualysException e) {
                log.warn(e.getMessage());
            }
        }));

        Page<WasScan> scansFiltered = new PageImpl<WasScan>(scans.getContent(), pageable, scansWsip
                .getTotalElements());

        return scansFiltered;
    }

    public Page<WasScan> search(Pageable pageable, Filters filters) throws QualysException {

        Page<WasScan> scans = search(pageable, filters, "/search/was/wasscan");

        List<WasScan> scanList = scans.getContent().stream().filter(scan -> scanRepository.findByScanToolId(scan.getId()
                .toString()).isPresent()).map(scan -> {
                    try {
                        return read(scan.getId());
                    } catch (QualysException e) {
                        log.warn(e.getMessage());
                    }
                    return scan;
                }).collect(Collectors.toList());

        Integer numberFiltered = scans.getContent().size() - scanList.size();

        Long countFiltered = scans.getTotalElements() - numberFiltered;

        Page<WasScan> scansFiltered = new PageImpl<WasScan>(scanList, pageable, countFiltered);

        return scansFiltered;
    }

}
