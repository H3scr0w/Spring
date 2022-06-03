
package com.saintgobain.dsi.starter.incapsula.model;

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
@ApiModel(value = "WAF Action", description = "WAF Action Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "requestResult",
        "isSecured",
        "url",
        "threats"
})
public class Action {

    @JsonProperty("requestResult")
    private String requestResult;

    @JsonProperty("isSecured")
    private Boolean isSecured;

    @JsonProperty("url")
    private String url;

    @JsonProperty("threats")
    private List<Threat> threats;

}
