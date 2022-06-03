package com.saintgobain.dsi.starter.qualys.bean.wasscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "WasScanMode", description = "WasScanMode Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum WasScanMode {
    ONDEMAND, SCHEDULED, API
}
