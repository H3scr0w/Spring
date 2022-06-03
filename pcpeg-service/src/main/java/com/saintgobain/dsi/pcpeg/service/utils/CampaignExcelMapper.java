package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.saintgobain.dsi.pcpeg.domain.CampaignView;
import com.saintgobain.dsi.pcpeg.dto.CampaignExcelDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CampaignExcelMapper implements Function<CampaignView, CampaignExcelDTO> {

	@Override
    public CampaignExcelDTO apply(CampaignView campaignView) {

        CampaignExcelDTO.CampaignExcelDTOBuilder builder = CampaignExcelDTO.builder()
                .codeSif(campaignView.getCodeSif())
                .societeLibelle(campaignView.getSocieteLibelle())
                .statutLibelle(campaignView.getStatutLibelle())
                .dateDernierMail(campaignView.getDateDernierMail())
                .flagEnvoieMail(campaignView.getFlagEnvoieMail())
                .flagRelanceMail(campaignView.getFlagRelanceMail())
                .correspondantActuelEmail(campaignView.getCorrespondantActuelEmail())
                .correspondantPrecedentEmail(campaignView.getCorrespondantPrecedentEmail());

        if (StringUtils.isNotBlank(campaignView.getCorrespondantActuelNom()) && StringUtils.isNotBlank(campaignView
                .getCorrespondantActuelPrenom())) {
            builder.correspondantN(StringUtils.upperCase(campaignView.getCorrespondantActuelNom()) + StringUtils.SPACE
                    + StringUtils.capitalize(campaignView.getCorrespondantActuelPrenom()));
        }

        if (StringUtils.isNotBlank(campaignView.getCorrespondantPrecedentNom()) && StringUtils.isNotBlank(campaignView
                .getCorrespondantPrecedentPrenom())) {
            builder.correspondantN1(StringUtils.upperCase(campaignView.getCorrespondantPrecedentNom())
                    + StringUtils.SPACE + StringUtils.capitalize(campaignView.getCorrespondantPrecedentPrenom()));
        }

        return builder.build();
	}

}
