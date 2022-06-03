package com.saintgobain.dsi.pcpeg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CompanySettingValidationDTO", description = "CompanySettingValidation DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class CompanySettingValidationDTO {

    private PaymentDTO payment;

    private List<DocumentDTO> documents;

    private FundDTO defaultFund;

    private List<FundDTO> funds;

}
