package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "DocrootEnvironmentDetail", description = "DocrootEnvironmentDetail Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NotNull
public class DocrootEnvironmentDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String cmsCode;

    private String cmsVersion;

    @NotNull
    private String drupalDocrootCoreCode;

    private String drupalDocrootCoreVersion;

    @Builder.Default
    private Boolean canAutoDeploy = false;

    private String providerInternalId;

    private String acquiaEnvironmentId;

}
