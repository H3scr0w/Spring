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
@ApiModel(value = "Cms", description = "Cms Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cms implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Cms name")
    private String name;

    @NotNull
    @ApiModelProperty(value = "The url of the CMS source code")
    private String codeRepositoryUrl;

    @NotNull
    @ApiModelProperty(value = "The url of the CMS binary")
    private String binaryRepositoryUrl;

    @ApiModelProperty(value = "is the cms enable")
    private Boolean enable;

}
