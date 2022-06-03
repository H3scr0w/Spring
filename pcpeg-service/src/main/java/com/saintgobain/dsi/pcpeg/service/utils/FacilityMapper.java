package com.saintgobain.dsi.pcpeg.service.utils;

import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class FacilityMapper implements Function<PeDimEtablissement, FacilityDTO> {

    @Override
    public FacilityDTO apply(PeDimEtablissement peDimEtablissement) {
        return FacilityDTO.builder()
                .facilityId(peDimEtablissement != null? peDimEtablissement.getId().getFacilityId() : null)
                .facilityLabel(peDimEtablissement != null? peDimEtablissement.getFacilityLabel() : null)
                .isActive(peDimEtablissement != null ? peDimEtablissement.getIsActive() : null)
                .codeSif(peDimEtablissement != null && peDimEtablissement.getSociete() != null ? peDimEtablissement.getSociete().getCodeSif(): null)
                .companyLabel(peDimEtablissement != null && peDimEtablissement.getSociete() != null ? peDimEtablissement.getSociete().getSocieteLibelle(): null)
                .build();
    }
}
