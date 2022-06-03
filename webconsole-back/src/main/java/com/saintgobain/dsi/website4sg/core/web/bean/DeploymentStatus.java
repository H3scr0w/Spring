package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "DeploymentStatus", description = "DeploymentStatus Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String deploymentStatusId;

    private String label;
}
