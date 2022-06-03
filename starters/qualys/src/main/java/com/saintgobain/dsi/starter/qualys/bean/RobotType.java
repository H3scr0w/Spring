package com.saintgobain.dsi.starter.qualys.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "RobotType", description = "RobotType Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum RobotType {
    IGNORE, ADD_PATHS, BLACKLIST
}
