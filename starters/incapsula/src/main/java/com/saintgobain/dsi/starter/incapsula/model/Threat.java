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
@ApiModel(value = "WAF Threat", description = "WAF Threat Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "incidents",
        "status",
        "status_text_id",
        "status_text",
        "followup",
        "followup_text",
        "followup_url",
        "securityRule",
        "alertLocation",
        "attackCodes",
        "securityRuleAction"
})
public class Threat {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("incidents")
    private Long incidents;

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_text_id")
    private String statusTextId;

    @JsonProperty("status_text")
    private String statusText;

    @JsonProperty("followup")
    private String followup;

    @JsonProperty("followup_text")
    private String followupText;

    @JsonProperty("followup_url")
    private String followupUrl;

    @JsonProperty("securityRule")
    private String securityRule;

    @JsonProperty("alertLocation")
    private String alertLocation;

    @JsonProperty("attackCodes")
    private List<String> attackCodes;

    @JsonProperty("securityRuleAction")
    private String securityRuleAction;

}
