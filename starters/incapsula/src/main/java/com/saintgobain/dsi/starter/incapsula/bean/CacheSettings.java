package com.saintgobain.dsi.starter.incapsula.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WAF CacheSettings", description = "WAF CacheSettings Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private CacheSettingParams param;

    @NotNull
    private Boolean value;

}
