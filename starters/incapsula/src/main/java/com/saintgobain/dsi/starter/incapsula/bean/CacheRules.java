package com.saintgobain.dsi.starter.incapsula.bean;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value = "WAF CacheRules", description = "WAF CacheRules Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@NotNull
public class CacheRules implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> cacheHeaders;

    // ALWAYS CACHE

    private List<String> alwaysCacheResourceUrl;

    private List<UrlPatterns> alwaysCacheResourcePattern;

    private Integer alwaysCacheResourceDuration;

    // always_cache_resource_duration = alwaysCacheResourceDuration + '_' + durationPeriod.name()
    private DurationPeriod durationPeriod;

    // NEVER CACHE

    private List<String> neverCacheResourceUrl;

    private List<UrlPatterns> neverCacheResourcePattern;

    // CLEAR

    private Boolean clearAlwaysCacheRules;

    private Boolean clearNeverCacheRules;

    private Boolean clearCacheHeadersRules;

}
