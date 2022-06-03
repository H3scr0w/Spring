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
@ApiModel(value = "WAF PerformanceConfiguration", description = "WAF PerformanceConfiguration Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "advanced_caching_rules",
        "acceleration_level",
        "async_validation",
        "minify_javascript",
        "minify_css",
        "minify_static_html",
        "compress_jpeg",
        "progressive_image_rendering",
        "aggressive_compression",
        "compress_png",
        "on_the_fly_compression",
        "tcp_pre_pooling",
        "comply_no_cache",
        "comply_vary",
        "use_shortest_caching",
        "support_all_tls_versions",
        "prefer_last_modified",
        "disable_client_side_caching",
        "cache_headers"
})
public class PerformanceConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("advanced_caching_rules")
    private AdvancedCachingRules advancedCachingRules;

    @JsonProperty("acceleration_level")
    private String accelerationLevel;

    @JsonProperty("async_validation")
    private Boolean asyncValidation;

    @JsonProperty("minify_javascript")
    private Boolean minifyJavascript;

    @JsonProperty("minify_css")
    private Boolean minifyCss;

    @JsonProperty("minify_static_html")
    private Boolean minifyStaticHtml;

    @JsonProperty("compress_jpeg")
    private Boolean compressJpeg;

    @JsonProperty("progressive_image_rendering")
    private Boolean progressiveImageRendering;

    @JsonProperty("aggressive_compression")
    private Boolean aggressiveCompression;

    @JsonProperty("compress_png")
    private Boolean compressPng;

    @JsonProperty("on_the_fly_compression")
    private Boolean onTheFlyCompression;

    @JsonProperty("tcp_pre_pooling")
    private Boolean tcpPrePooling;

    @JsonProperty("comply_no_cache")
    private Boolean complyNoCache;

    @JsonProperty("comply_vary")
    private Boolean complyVary;

    @JsonProperty("use_shortest_caching")
    private Boolean useShortestCaching;

    @JsonProperty("support_all_tls_versions")
    private Boolean supportAllTlsVersions;

    @JsonProperty("prefer_last_modified")
    private Boolean preferLastModified;

    @JsonProperty("disable_client_side_caching")
    private Boolean disableClientSideCaching;

    @JsonProperty("cache_headers")
    private List<CacheHeader> cacheHeaders;

}
