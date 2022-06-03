package com.saintgobain.dsi.starter.incapsula.model;

import java.io.Serializable;

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
@ApiModel(value = "WAF AlwaysCacheResource", description = "WAF AlwaysCacheResource Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "pattern",
        "url",
        "ttl",
        "ttlUnits"
})
public class AlwaysCacheResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("url")
    private String url;

    @JsonProperty("ttl")
    private Long ttl;

    @JsonProperty("ttlUnits")
    private String ttlUnits;

}
