package com.saintgobain.dsi.website4sg.core.web.bean;

import java.util.List;

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
@ApiModel(value = "WebsiteDeployedBody", description = "WebsiteDeployedBody Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvironmentDetail {

    private String environmentCode;

    private String name;

    private String websiteVersion;

    private String cmsVersion;

    private String cmsCode;

    private String cmsName;

    private List<DomainHeader> domains;

}
