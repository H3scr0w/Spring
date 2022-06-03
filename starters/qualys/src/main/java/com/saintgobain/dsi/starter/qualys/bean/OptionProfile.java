package com.saintgobain.dsi.starter.qualys.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "OptionProfile", description = "OptionProfile Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private User owner;

    private Boolean isDefault;

    private Long timeoutErrorThreshold;

    private Long unexpectedErrorThreshold;

}
