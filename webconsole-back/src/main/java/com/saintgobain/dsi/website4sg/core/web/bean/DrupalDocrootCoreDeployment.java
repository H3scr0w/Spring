package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "DrupalDocrootCoreDeployment", description = "DrupalDocrootCoreDeployment Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrupalDocrootCoreDeployment extends ProjectDeployment implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String drupalDocrootCoreCode;

}
