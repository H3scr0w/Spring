package com.saintgobain.dsi.starter.qualys.bean;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "SetWrapper", description = "SetWrapper Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetWrapper<T> {

    @JsonProperty("WebAppAuthServerRecordField")
    private Set<T> webAppAuthServerRecordField;

    @JsonProperty("WebAppAuthRecord")
    private Set<T> webAppAuthRecord;

}
