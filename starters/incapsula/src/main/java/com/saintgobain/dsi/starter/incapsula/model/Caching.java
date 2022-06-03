package com.saintgobain.dsi.starter.incapsula.model;

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
@ApiModel(value = "WAF Caching", description = "WAF Caching Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "saved_requests",
        "total_requests",
        "saved_bytes",
        "total_bytes"
})
public class Caching {

    @JsonProperty("saved_requests")
    private Long savedRequests;

    @JsonProperty("total_requests")
    private Long totalRequests;

    @JsonProperty("saved_bytes")
    private Long savedBytes;

    @JsonProperty("total_bytes")
    private Long totalBytes;

}
