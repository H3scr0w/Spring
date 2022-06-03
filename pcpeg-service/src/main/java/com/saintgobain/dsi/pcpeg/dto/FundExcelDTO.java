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
@ApiModel(value = "FundExcel DTO", description = "FundExcel DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundExcelDTO {

    private String amundiCode;

    private String fundLabel;

    private String teneurCompteLibelle;

    private String fundGroupId;

    private Boolean isActive;

}
