package com.saintgobain.dsi.starter.qualys.bean.wasscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "WasScanType", description = "WasScanType Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum WasScanType {
    VULNERABILITY, DISCOVERY
}
