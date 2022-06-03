package com.saintgobain.dsi.starter.qualys.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ServiceRequest", description = "ServiceRequest Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
public class ServiceRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
    private T data;

    private Filters filters;

    private Preferences preferences;
}
