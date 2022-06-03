package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "AccessRightBody", description = "AccessRightBody Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessRightBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long accessRightId;

    @NotNull
    private String userMail;

    @NotNull
    private String roleLabel;

    @NotNull
    private String projectCode;

    @NotNull
    @ApiModelProperty(value = "Project Type", allowableValues = "ddc, w", required = true)
    private String projectType;

    private String projectName;

}
