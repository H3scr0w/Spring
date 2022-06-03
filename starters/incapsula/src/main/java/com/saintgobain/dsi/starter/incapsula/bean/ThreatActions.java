package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ThreatActions", description = "ThreatActions Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ThreatActions implements Securities {

    // To precede with "api.threats.action." before call

    disabled, alert, block_request, block_user, block_ip, quarantine_url
}
