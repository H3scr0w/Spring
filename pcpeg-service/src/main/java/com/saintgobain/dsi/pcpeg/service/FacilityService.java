package com.saintgobain.dsi.pcpeg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissementId;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;
import com.saintgobain.dsi.pcpeg.repository.FacilityRepository;
import com.saintgobain.dsi.pcpeg.service.utils.ExcelUtils;
import com.saintgobain.dsi.pcpeg.service.utils.FacilityMapper;
import com.saintgobain.dsi.pcpeg.service.utils.SortUtil;
import com.saintgobain.dsi.pcpeg.service.utils.StreamUtils;
import com.saintgobain.dsi.pcpeg.specification.FacilitySpecification;

import lombok.RequiredArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.BooleanUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Page<FacilityDTO> getAllFacilities(Pageable pageable, String label, Boolean isActive,
            Integer societeSid, String codeSif) {
        Specification<PeDimEtablissement> spec = generateSpecification(label, isActive, societeSid, codeSif);
        pageable = SortUtil.ignoreCase(pageable);
        pageable = SortUtil.andSort(pageable, "id.societeSid");
        pageable = SortUtil.andSort(pageable, "id.facilityId");
        Page<PeDimEtablissement> results = facilityRepository.findAll(spec, pageable);
        return mapListToDtos(results);
    }

    @Transactional(readOnly = true)
    public byte[] downloadFacilities(String label, Boolean isActive, Integer societeSid, String codeSif) {
        byte[] getBytes = new byte[] {};
        Page<FacilityDTO> peDimEtablissements = getAllFacilities(Pageable.unpaged(), label, isActive, societeSid,
                codeSif);
        if (peDimEtablissements != null && CollectionUtils.isNotEmpty(peDimEtablissements.getContent())) {
            getBytes = new ExcelUtils().writeToExcel(peDimEtablissements.getContent(), "Facilities").readAllBytes();
        }

        return getBytes;
    }

    @Transactional
    public PeDimEtablissement createOrUpdateFacility(FacilityDTO facilityDTO) {
        PeDimSociete peDimSociete = companyRepository.findFirstByCodeSif(facilityDTO.getCodeSif()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Societe not found for codeSif: " + facilityDTO.getCodeSif()));
        PeDimEtablissement dbPeDimEtablissement = facilityRepository.findById_FacilityIdAndId_SocieteSid(facilityDTO
                .getFacilityId(), peDimSociete.getSocieteSid()).orElse(null);
        if (dbPeDimEtablissement != null) {
            dbPeDimEtablissement.setFacilityLabel(facilityDTO.getFacilityLabel().trim());
            dbPeDimEtablissement.setIsActive(facilityDTO.getIsActive());
            return facilityRepository.save(dbPeDimEtablissement);
        } else {
            PeDimEtablissementId peDimEtablissementId = new PeDimEtablissementId();
            peDimEtablissementId.setFacilityId(facilityDTO.getFacilityId());
            peDimEtablissementId.setSocieteSid(peDimSociete.getSocieteSid());
            PeDimEtablissement peDimEtablissement = new PeDimEtablissement();
            peDimEtablissement.setId(peDimEtablissementId);
            peDimEtablissement.setFacilityShortLabel("(" + facilityDTO.getFacilityId() + ")");
            peDimEtablissement.setFacilityLabel(facilityDTO.getFacilityLabel().trim());
            peDimEtablissement.setIsActive(facilityDTO.getIsActive());
            return facilityRepository.save(peDimEtablissement);
        }
    }

    @Transactional(readOnly = true)
    public List<FacilityDTO> getDistinctCompanies(Pageable pageable, String codeSif, String companyLabel) {
        List<FacilityDTO> results = null;
        Specification<PeDimEtablissement> spec = Specification.where(null);
        if (StringUtils.isNotBlank(codeSif)) {
            spec = spec.and(FacilitySpecification.codeSifSpecification(codeSif));
        }
        if (StringUtils.isNotBlank(companyLabel)) {
            spec = spec.and(FacilitySpecification.companyLabelSpecification(companyLabel));
        }
        results = facilityRepository.findAll(spec, pageable)
                .stream()
                .filter(Objects::nonNull)
                .filter(StreamUtils.distinctByKey(f -> f.getSociete().getCodeSif()))
                .map(new FacilityMapper())
                .collect(Collectors.toList());

        return results;
    }

    private Page<FacilityDTO> mapListToDtos(Page<PeDimEtablissement> facilities) {
        if (facilities != null) {
            facilities.forEach(facility -> {
                if (StringUtils.equals(facility.getIsActive(), "O")) {
                    facility.setIsActive("Yes");
                } else {
                    facility.setIsActive("No");
                }
            });
            return facilities.map(new FacilityMapper());
        }
        return new PageImpl<>(new ArrayList<>());
    }

    private Specification<PeDimEtablissement> generateSpecification(String label, Boolean isActive,
            Integer societeSid, String codeSif) {
        Specification<PeDimEtablissement> spec = Specification.where(null);

        if (StringUtils.isNotBlank(label)) {
            spec = spec.and(FacilitySpecification.labelSpecification(label));
        }

        if (BooleanUtils.isTrue(isActive)) {
            spec = spec.and(FacilitySpecification.activeSpecification());
        }

        if (societeSid != null && societeSid != 0) {
            spec = spec.and(FacilitySpecification.societeSidSpecification(societeSid));
        }

        if (StringUtils.isNotBlank(codeSif)) {
            spec = spec.and(FacilitySpecification.codeSifSpecification(codeSif));
        }

        return spec;
    }
}
