package com.saintgobain.dsi.starter.qualys.bean.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ReportType", description = "ReportType Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ReportType {
    WAS_SCAN_REPORT, WAS_WEBAPP_REPORT, WAS_SCORECARD_REPORT, WAS_CATALOG_REPORT, DATALIST_REPORT
}
