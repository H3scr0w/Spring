package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Continents", description = "Continents Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum Continents {
    AF, NA, OC, AN, AS, EU, SA
}
