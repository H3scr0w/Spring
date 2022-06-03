package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.function.Function;

import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;
import com.saintgobain.dsi.pcpeg.dto.AuthoritySettingDTO;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthoritySettingMapper implements Function<PeParHabilitations, AuthoritySettingDTO> {
    @Override
    public AuthoritySettingDTO apply(PeParHabilitations peParHabilitations) {


        return AuthoritySettingDTO.builder()
                .id(peParHabilitations != null ? peParHabilitations.getHabilitationsSid() : null)
                .name(peParHabilitations != null ? peParHabilitations.getNom() : null)
                .firstname(peParHabilitations != null ? peParHabilitations.getPrenom() : null)
                .sgid(peParHabilitations != null ? peParHabilitations.getSgid() : null)
                .email(peParHabilitations != null ? peParHabilitations.getEmail() : null)
                .telephone(peParHabilitations != null ? peParHabilitations.getTelephone() : null)
                .facility(peParHabilitations.getEtablissement() != null ? FacilityDTO
                        .builder()
                        .facilityId(peParHabilitations.getEtablissement().getId().getFacilityId())
                        .codeSif(peParHabilitations.getEtablissement().getSociete().getCodeSif())
                        .facilityLabel(peParHabilitations.getEtablissement().getFacilityLabel())
                        .isActive(peParHabilitations.getEtablissement().getIsActive())
                        .build() : null)
                .category(peParHabilitations.getCategory())
                .year(peParHabilitations != null && peParHabilitations.getPeParSociete() != null && peParHabilitations.getPeParSociete().getPeDimAnnee() != null ?
                        peParHabilitations.getPeParSociete().getPeDimAnnee().getAnneeId().toString() : null).build();
    }
}
