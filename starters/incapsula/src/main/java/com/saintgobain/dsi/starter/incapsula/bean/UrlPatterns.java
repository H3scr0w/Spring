package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "UrlPatterns", description = "UrlPatterns Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum UrlPatterns {

    contains, equals, prefix, suffix, not_equals, not_contain, not_prefix, not_suffix
}
