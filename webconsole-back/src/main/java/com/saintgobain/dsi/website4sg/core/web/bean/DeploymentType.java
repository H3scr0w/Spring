package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deploymentTypeId;

    private String label;

}
