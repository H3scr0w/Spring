package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "ProjectDeployment", description = "ProjectDeployment Object", subTypes = {
        DrupalWebsiteDeployment.class,
        DrupalDocrootCoreDeployment.class })
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDeployment implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    protected String deploymentTypeId;

    @NotNull
    protected String binaryRepositoryUrl;

    protected String versionToDeploy;

    @Builder.Default
    protected List<CommandHeader> commands = new ArrayList<CommandHeader>();

    protected String docrootCode;

    protected String rundeckJobId;

    protected String environmentCode;


}
