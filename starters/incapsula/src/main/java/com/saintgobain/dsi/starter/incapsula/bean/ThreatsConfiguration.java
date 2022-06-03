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
@ApiModel(value = "WAF ThreatsConfiguration", description = "WAF ThreatsConfiguration Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class ThreatsConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private ThreatRules ruleId;

    private ThreatActions securityRuleAction;

    private Boolean blockBadBots;

    private Boolean challengeSuspectedBots;

    // Only for DDOS rule
    private ActivationMode activationMode;

    private Integer ddosTrafficThreshold;
    // END DDOS

    // Only for URL ruleId = backdoor and securityRuleAction = quarantine_url
    private List<String> quarantinedUrls;
    // END URL

}
