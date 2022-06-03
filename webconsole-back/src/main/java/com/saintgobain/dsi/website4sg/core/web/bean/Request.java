package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;
import java.util.List;

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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel(value = "Request", description = "Request Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "Deployment Type", allowableValues = "D_DOCROOTCORE, D_WEBSITE", required = true)
    private String deploymentTypeId;

    @NotNull
    @ApiModelProperty(value = "Docroot code", required = true)
    private String docrootCode;

    @NotNull
    @ApiModelProperty(value = "Environment code", required = true)
    private String environmentCode;

    @NotNull
    @ApiModelProperty(value = "Project code", required = true)
    private String deliverableCode;

    private String deliverableVersion;

    private List<Command> commands;

}
