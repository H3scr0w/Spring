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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "Website", description = "Website Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Website implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    public String codeRepositoryUrl;

    @NotNull
    public String binaryRepositoryUrl;

    @NotNull
    public String homeDirectory;

    @NotNull
    private String name;

    public Boolean enable;

    private Long qualysWebAppId;

    private Boolean isQualysEnable;

    @Builder.Default
    private Boolean isLive = false;
}