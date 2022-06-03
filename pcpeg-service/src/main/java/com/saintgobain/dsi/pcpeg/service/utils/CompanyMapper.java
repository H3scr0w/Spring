package com.saintgobain.dsi.pcpeg.service.utils;

import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.dto.PeDimSocieteDTO;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class CompanyMapper implements Function<PeDimSociete, PeDimSocieteDTO> {

    @Override
    public PeDimSocieteDTO apply(PeDimSociete peDimSociete) {

        return PeDimSocieteDTO.builder()
            .societeSid(peDimSociete.getSocieteSid())
            .societeLibelle(peDimSociete.getSocieteLibelle())
            .codeSif(peDimSociete.getCodeSif())
            .codeAmundi(peDimSociete.getCodeAmundi())
            .flagAdherente(peDimSociete.getFlagAdherente())
            .comments(peDimSociete.getComments())
            .cspId(peDimSociete.getCspId())
            .build();
    }

}
