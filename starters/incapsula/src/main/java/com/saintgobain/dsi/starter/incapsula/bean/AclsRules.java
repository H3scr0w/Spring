package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "AclsRules", description = "AclsRules Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum AclsRules {

    // To precede with "api.acl." before call

    blacklisted_countries, blacklisted_urls, blacklisted_ips, whitelisted_ips
}
