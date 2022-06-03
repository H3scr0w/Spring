package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "StatusTests", description = "StatusTests Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum StatusTests {

    domain_validation, services, dns

}
