package com.saintgobain.dsi.pcpeg.dto;

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
@ApiModel(value = "CampaignStatsDTO", description = "CampaignStats DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignStatsDTO {

    private Long companyAdherentCount;

    private Long companyValidatedCount;

}
