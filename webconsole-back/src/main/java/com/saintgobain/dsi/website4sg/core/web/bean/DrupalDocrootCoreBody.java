package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@ApiModel(value = "DrupalDocrootCoreBody", description = "DrupalDocrootCoreBody Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrupalDocrootCoreBody implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private String codeRepositoryUrl;

    @NotNull
    @ApiModelProperty(hidden = true)
    private String binaryRepositoryUrl;

}
