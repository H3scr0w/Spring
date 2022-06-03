package com.saintgobain.dsi.pcpeg.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FacilityDTO", description = "Facility DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = JsonInclude.Include.NON_NULL)
public class FacilityDTO {

    @NotBlank
    @Size(min = 5, max = 5)
    private String facilityId;

    @NotBlank
    private String facilityLabel;

    @NotBlank
    private String isActive;

    @NotNull
    @Size(min = 5, max = 5)
    private String codeSif;

    private String companyLabel;
}
