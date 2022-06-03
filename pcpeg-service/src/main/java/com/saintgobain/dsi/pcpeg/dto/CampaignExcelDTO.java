package com.saintgobain.dsi.pcpeg.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CampaignExcelDTO", description = "CampaignExcel DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignExcelDTO {

    private String codeSif;

    private String societeLibelle;

    private String statutLibelle;

    private Date dateDernierMail;

    private Boolean flagEnvoieMail;

    private Boolean flagRelanceMail;

    private String correspondantN;

    private String correspondantActuelEmail;

    private String correspondantN1;

    private String correspondantPrecedentEmail;

}
