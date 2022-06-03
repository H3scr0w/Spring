package com.saintgobain.dsi.website4sg.core.web.bean;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DomainLoadBalancer", description = "DomainLoadBalancer Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainLoadBalancer {

    private String domainCode;

    private String domainName;

    private String loadBalancerCode;

    private String loadBalancerName;

    @NotNull
    private String websiteCode;

    private String websiteName;
}
