
package com.saintgobain.dsi.starter.incapsula.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WAF SecuritySummary", description = "WAF SecuritySummary Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "api.threats.sql_injection",
        "api.threats.cross_site_scripting",
        "api.threats.illegal_resource_access",
        "api.threats.remote_file_inclusion",
        "api.threats.customRule",
        "api.threats.ddos",
        "api.threats.backdoor",
        "api.threats.bot_access_control",
        "api.acl.blacklisted_countries",
        "api.acl.blacklisted_urls",
        "api.acl.blacklisted_ips"
})
public class SecuritySummary {

    @JsonProperty("api.threats.sql_injection")
    private Long apiThreatsSqlInjection;

    @JsonProperty("api.threats.cross_site_scripting")
    private Long apiThreatsCrossSiteScripting;

    @JsonProperty("api.threats.illegal_resource_access")
    private Long apiThreatsIllegalResourceAccess;

    @JsonProperty("api.threats.remote_file_inclusion")
    private Long apiThreatsRemoteFileInclusion;

    @JsonProperty("api.threats.customRule")
    private Long apiThreatsCustomRule;

    @JsonProperty("api.threats.ddos")
    private Long apiThreatsDdos;

    @JsonProperty("api.threats.backdoor")
    private Long apiThreatsBackdoor;

    @JsonProperty("api.threats.bot_access_control")
    private Long apiThreatsBotAccessControl;

    @JsonProperty("api.acl.blacklisted_countries")
    private Long apiAclBlacklistedCountries;

    @JsonProperty("api.acl.blacklisted_urls")
    private Long apiAclBlacklistedUrls;

    @JsonProperty("api.acl.blacklisted_ips")
    private Long apiAclBlacklistedIps;

}
