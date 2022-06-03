
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
@ApiModel(value = "WAF Visit", description = "WAF Visit Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "id",
        "siteId",
        "startTime",
        "clientIPs",
        "country",
        "countryCode",
        "clientType",
        "clientApplication",
        "clientApplicationVersion",
        "httpVersion",
        "userAgent",
        "os",
        "osVersion",
        "supportsCookies",
        "supportsJavaScript",
        "hits",
        "pageViews",
        "entryReferer",
        "entryPage",
        "servedVia",
        "securitySummary",
        "actions"
})
public class Visit {

    @JsonProperty("id")
    private String id;

    @JsonProperty("siteId")
    private Long siteId;

    @JsonProperty("startTime")
    private Long startTime;

    @JsonProperty("clientIPs")
    private List<String> clientIPs;

    @JsonProperty("country")
    private List<String> country;

    @JsonProperty("countryCode")
    private List<String> countryCode;

    @JsonProperty("clientType")
    private String clientType;

    @JsonProperty("clientApplication")
    private String clientApplication;

    @JsonProperty("clientApplicationVersion")
    private String clientApplicationVersion;

    @JsonProperty("httpVersion")
    private String httpVersion;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("os")
    private String os;

    @JsonProperty("osVersion")
    private String osVersion;

    @JsonProperty("supportsCookies")
    private Boolean supportsCookies;

    @JsonProperty("supportsJavaScript")
    private Boolean supportsJavaScript;

    @JsonProperty("hits")
    private Long hits;

    @JsonProperty("pageViews")
    private Long pageViews;

    @JsonProperty("entryReferer")
    private String entryReferer;

    @JsonProperty("entryPage")
    private String entryPage;

    @JsonProperty("servedVia")
    private List<String> servedVia;

    @JsonProperty("securitySummary")
    private SecuritySummary securitySummary;

    @JsonProperty("actions")
    private List<Action> actions;

}
