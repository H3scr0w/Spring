package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "CacheSettingParams", description = "CacheSettingParams Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum CacheSettingParams {
    async_validation,

    minify_javascript,

    minify_css,

    minify_static_html,

    compress_jpeg,

    progressive_image_rendering,

    aggressive_compression,

    compress_png,

    on_the_fly_compression,

    tcp_pre_pooling,

    comply_no_cache,

    comply_vary,

    use_shortest_caching,

    prefer_last_modified,

    disable_client_side_caching,

    cache_300x
}
