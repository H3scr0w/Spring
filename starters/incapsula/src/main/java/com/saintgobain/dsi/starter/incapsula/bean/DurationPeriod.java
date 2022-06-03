package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "DurationPeriod", description = "DurationPeriod Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum DurationPeriod {
    hr, min, sec, days, weeks
}
