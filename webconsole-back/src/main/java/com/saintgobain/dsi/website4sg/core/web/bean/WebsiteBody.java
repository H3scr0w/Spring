package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;
import java.util.Date;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WebsiteBody", description = "WebsiteBody Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebsiteBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long websiteId;

    @NotNull
    private String codeRepositoryUrl;

    @NotNull
    @ApiModelProperty(hidden = true)
    private String binaryRepositoryUrl;

    @NotNull
    private String homeDirectory;

    @NotNull
    private String name;

    private Boolean enable;

    private Date created;

    private Date lastUpdate;

    @NotNull
    private String code;

    @ApiModelProperty(hidden = true)
    private Long qualysWebAppId;

    @ApiModelProperty(hidden = true)
    private Boolean isQualysEnable;

    private Boolean isLive;

}
