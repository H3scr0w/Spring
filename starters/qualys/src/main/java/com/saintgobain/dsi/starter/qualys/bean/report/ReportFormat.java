package com.saintgobain.dsi.starter.qualys.bean.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ReportFormat", description = "ReportFormat Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ReportFormat {
    HTML_ZIPPED, HTML_BASE64, PDF, PDF_ENCRYPTED, POWERPOINT, CSV, CSV_V2, XML, WORD
}
