package com.saintgobain.dsi.pcpeg.dto;

import javax.validation.constraints.NotBlank;

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
@ApiModel(value = "CorrespondantDTO", description = "Correspondant DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorrespondantDTO {

    @NotBlank
    private String societeId;

    @NotBlank
    private String year;

    @NotBlank
    private String userId;

}
