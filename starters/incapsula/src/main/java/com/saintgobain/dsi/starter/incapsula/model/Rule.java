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
@ApiModel(value = "WAF Rules", description = "WAF Rules Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "id",
        "name",
        "block_bad_bots",
        "challenge_suspected_bots",
        "action",
        "action_text",
        "exceptions",
        "activation_mode",
        "activation_mode_text",
        "ddos_traffic_threshold",
        "ddos_traffic_threshold_text",
        "geo",
        "urls",
        "ips"
})
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("block_bad_bots")
    private Boolean blockBadBots;

    @JsonProperty("challenge_suspected_bots")
    private Boolean challengeSuspectedBots;

    @JsonProperty("action")
    private String action;

    @JsonProperty("action_text")
    private String actionText;

    @JsonProperty("exceptions")
    private List<Exception> exceptions;

    @JsonProperty("activation_mode")
    private String activationMode;

    @JsonProperty("activation_mode_text")
    private String activationModeText;

    @JsonProperty("ddos_traffic_threshold")
    private Integer ddosTrafficThreshold;

    @JsonProperty("ddos_traffic_threshold_text")
    private String ddosTrafficThresholdText;

    @JsonProperty("geo")
    private Geo geo;

    @JsonProperty("urls")
    private List<Url> urls;

    @JsonProperty("ips")
    private List<String> ips;

}
