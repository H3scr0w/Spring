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
@ApiModel(value = "LoadBalancer", description = "LoadBalancer Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadBalancer {

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String ip;

    private String ip2;

    private String fqdn;

    @NotNull
    private String hostingProviderCode;

    private String hostingProviderName;

}
