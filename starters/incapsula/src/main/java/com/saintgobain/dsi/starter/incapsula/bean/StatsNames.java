package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "StatsNames", description = "StatsNames Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum StatsNames {
    visits_timeseries,

    hits_timeseries,

    bandwidth_timeseries,

    requests_geo_dist_summary,

    visits_dist_summary,

    caching,

    caching_timeseries,

    threats,

    incap_rules,

    incap_rules_timeseries
}
