package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "TimeRanges", description = "TimeRanges Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum TimeRanges {
    today,

    last_7_days,

    last_30_days,

    last_90_days,

    month_to_date
}
