package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "CacheModes", description = "CacheModes Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum CacheModes {
    disable,

    static_only,

    // Use dynamicCacheDuration and durationPeriod if present
    static_and_dynamic,

    // Use aggressiveCacheDuration and durationPeriod if present
    aggressive
}
