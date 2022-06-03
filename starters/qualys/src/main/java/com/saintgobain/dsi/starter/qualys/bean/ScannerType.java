package com.saintgobain.dsi.starter.qualys.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ScannerType", description = "ScannerType Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ScannerType {
    INTERNAL, EXTERNAL

}
