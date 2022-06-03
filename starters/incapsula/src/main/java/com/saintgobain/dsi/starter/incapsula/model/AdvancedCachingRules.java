package com.saintgobain.dsi.starter.incapsula.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WAF AdvancedCachingRules", description = "WAF AdvancedCachingRules Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "never_cache_resources",
        "always_cache_resources"
})
public class AdvancedCachingRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("never_cache_resources")
    private List<NeverCacheResource> neverCacheResources;

    @JsonProperty("always_cache_resources")
    private List<AlwaysCacheResource> alwaysCacheResources;

}
