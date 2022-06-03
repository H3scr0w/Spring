package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NotNull
@ApiModel(value = "Deployment", description = "Deployment Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long deploymentId;

    private String docrootCode;

    private String environmentCode;

    // DDC_CODE or WEBS_CODE
    private String deliverableCode;

    private String deliverableVersion;

    private String requester;

    private String rundeckJobId;

    private DeploymentStatus deploymentStatus;

    private DeploymentType deploymentType;

    private Date creationDate;

}
