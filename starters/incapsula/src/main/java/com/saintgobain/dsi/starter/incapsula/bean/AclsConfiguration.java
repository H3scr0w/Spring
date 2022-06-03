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
@ApiModel(value = "WAF AclsConfiguration", description = "WAF AclsConfiguration Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class AclsConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private AclsRules ruleId;

    // Only for Blacklisted Urls rule
    private List<String> urls;

    private List<UrlPatterns> urlPatterns;

    // END Blacklisted Urls

    // Only for Blacklisted Countries rule
    private List<Countries> countries;

    private List<Continents> continents;

    // END Blacklisted Countries

    // Only for Blacklisted or Whitelisted Ips rule
    private List<String> ips;

    // END Blacklisted or Whitelisted Ips

}
