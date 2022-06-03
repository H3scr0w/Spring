package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.dto.FundExcelDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FundExcelMapper implements Function<FundDTO, FundExcelDTO> {

	@Override
    public FundExcelDTO apply(FundDTO fundDTO) {

        return FundExcelDTO.builder()
            .amundiCode(fundDTO.getAmundiCode())
            .fundLabel(fundDTO.getFundLabel())
            .teneurCompteLibelle(fundDTO.getTenantAccount() != null ? fundDTO.getTenantAccount().getTeneurCompteLibelle() : StringUtils.EMPTY)
            .fundGroupId(fundDTO.getFundGroupId())
            .isActive(fundDTO.getIsActive())
            .build();
    }

}
