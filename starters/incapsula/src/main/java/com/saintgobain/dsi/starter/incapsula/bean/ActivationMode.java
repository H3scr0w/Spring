package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ActivationMode", description = "ActivationMode Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ActivationMode {

    //
    // The syntax is as follows:<rule_id>.activation_mode.<value> For example, for 'off', use
    // 'activation_mode=api.threats.ddos.activation_mode.off'.

    off, auto, on
}
