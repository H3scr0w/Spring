package com.saintgobain.dsi.starter.qualys.bean.wasscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "WasScanStatus", description = "WasScanStatus Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum WasScanStatus {
    SUBMITTED, RUNNING, FINISHED, ERROR, CANCELED
}
