package com.saintgobain.dsi.website4sg.core.web.bean;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Environment", description = "Environment Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

    @NotNull
    private String name;

}
