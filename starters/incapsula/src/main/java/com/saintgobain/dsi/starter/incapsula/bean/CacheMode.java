package com.saintgobain.dsi.starter.incapsula.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
@ApiModel(value = "WAF CacheMode", description = "WAF CacheMode Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class CacheMode implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private CacheModes cacheMode;

    private Integer dynamicCacheDuration;

    private Integer aggressiveCacheDuration;

    private DurationPeriod durationPeriod;

}
