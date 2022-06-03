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
@ApiModel(value = "AuthoritySettingDTO", description = "AuthoritySetting DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthoritySettingDTO {

    private Short id;

    private String name;

    private String firstname;

    private String email;

    private String telephone;

    @NotBlank
    private String sgid;

    @NotBlank
    private String category;

    private String year;

    private FacilityDTO facility;

}
