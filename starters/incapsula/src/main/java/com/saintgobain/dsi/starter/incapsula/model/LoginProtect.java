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
@ApiModel(value = "WAF LoginProtect", description = "WAF LoginProtect Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "enabled",
        "specific_users_list",
        "send_lp_notifications",
        "allow_all_users",
        "authentication_methods",
        "urls",
        "url_patterns"
})
public class LoginProtect implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("specific_users_list")
    private List<SpecificUsersList> specificUsersList;

    @JsonProperty("send_lp_notifications")
    private Boolean sendLpNotifications;

    @JsonProperty("allow_all_users")
    private Boolean allowAllUsers;

    @JsonProperty("authentication_methods")
    private List<String> authenticationMethods;

    @JsonProperty("urls")
    private List<String> urls;

    @JsonProperty("url_patterns")
    private List<String> urlPatterns;

}
