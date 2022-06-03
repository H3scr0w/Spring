package com.saintgobain.dsi.website4sg.core.web.bean;

import java.util.List;

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

/**
 * @author R1304520
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Domain", description = "Domain Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Domain {

    /**
     * Principal
     */
    private String code;

    @NotNull
    private String domainType;

    private DomainHeader parent;

    private List<Domain> children;

    @NotNull
    private String name;

    @NotNull
    private String websiteCode;

    @NotNull
    private String docrootCode;

    @NotNull
    private String environmentCode;

    @ApiModelProperty(hidden = true)
    private String registarCode;

    @ApiModelProperty(hidden = true)
    private String registarName;

    private Boolean httpsEnable;

    /**
     * Authentication
     */

    @ApiModelProperty(hidden = true)
    private String realm;

    @ApiModelProperty(hidden = true)
    private String user;

    @ApiModelProperty(hidden = true)
    private String password;

    @ApiModelProperty(hidden = true)
    private Boolean useDocrootEnvAuth;

    @ApiModelProperty(hidden = true)
    private Boolean isBasicAuth;

    /**
     * Qualys
     */

    private Boolean isQualysEnable;

    @ApiModelProperty(hidden = true)
    private Long qualysWebAppId;

    @ApiModelProperty(hidden = true)
    private String qualysWebAuthId;

    /**
     * Waf
     */
    @ApiModelProperty(hidden = true)
    private String wafId;

    /**
     * Monitoring
     */

    private Boolean isMonitorEnable;

    @ApiModelProperty(hidden = true)
    private String monitorKeyword;

}
