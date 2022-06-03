package com.saintgobain.dsi.starter.incapsula.service;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.saintgobain.dsi.starter.incapsula.model.BandwidthTimeseries;
import com.saintgobain.dsi.starter.incapsula.model.Caching;
import com.saintgobain.dsi.starter.incapsula.model.CachingTimeseries;
import com.saintgobain.dsi.starter.incapsula.model.Dn;
import com.saintgobain.dsi.starter.incapsula.model.HitsTimeseries;
import com.saintgobain.dsi.starter.incapsula.model.LoginProtect;
import com.saintgobain.dsi.starter.incapsula.model.OriginalDn;
import com.saintgobain.dsi.starter.incapsula.model.PerformanceConfiguration;
import com.saintgobain.dsi.starter.incapsula.model.RequestsGeoDistSummary;
import com.saintgobain.dsi.starter.incapsula.model.SealLocation;
import com.saintgobain.dsi.starter.incapsula.model.Security;
import com.saintgobain.dsi.starter.incapsula.model.Ssl;
import com.saintgobain.dsi.starter.incapsula.model.Threat;
import com.saintgobain.dsi.starter.incapsula.model.Visit;
import com.saintgobain.dsi.starter.incapsula.model.VisitsDistSummary;
import com.saintgobain.dsi.starter.incapsula.model.VisitsTimeseries;
import com.saintgobain.dsi.starter.incapsula.model.Warning;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "IncapsulaResponse", description = "IncapsulaResponse Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "res",
        "res_message",
        "status",
        "ips",
        "dns",
        "original_dns",
        "warnings",
        "security",
        "active",
        "acceleration_level",
        "site_creation_date",
        "sealLocation",
        "ssl",
        "login_protect",
        "performance_configuration",
        "visits_timeseries",
        "requests_geo_dist_summary",
        "caching",
        "caching_timeseries",
        "hits_timeseries",
        "threats",
        "visits_dist_summary",
        "bandwidth_timeseries",
        "visits"
})
public class IncapsulaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("res")
    private Integer res;

    @JsonProperty("res_message")
    private String resMessage;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ips")
    private List<String> ips;

    @JsonProperty("dns")
    private List<Dn> dns;

    @JsonProperty("original_dns")
    private List<OriginalDn> originalDns;

    @JsonProperty("warnings")
    private List<Warning> warnings;

    @JsonProperty("security")
    private Security security;

    @JsonProperty("active")
    private String active;

    @JsonProperty("acceleration_level")
    private String accelerationLevel;

    @JsonProperty("site_creation_date")
    private Long siteCreationDate;

    @JsonProperty("sealLocation")
    private SealLocation sealLocation;

    @JsonProperty("ssl")
    private Ssl ssl;

    @JsonProperty("login_protect")
    private LoginProtect loginProtect;

    @JsonProperty("performance_configuration")
    private PerformanceConfiguration performanceConfiguration;

    @JsonProperty("visits_timeseries")
    private List<VisitsTimeseries> visitsTimeseries;

    @JsonProperty("requests_geo_dist_summary")
    private RequestsGeoDistSummary requestsGeoDistSummary;

    @JsonProperty("caching")
    private Caching caching;

    @JsonProperty("caching_timeseries")
    private List<CachingTimeseries> cachingTimeseries;

    @JsonProperty("hits_timeseries")
    private List<HitsTimeseries> hitsTimeseries;

    @JsonProperty("threats")
    private List<Threat> threats;

    @JsonProperty("visits_dist_summary")
    private List<VisitsDistSummary> visitsDistSummary;

    @JsonProperty("bandwidth_timeseries")
    private List<BandwidthTimeseries> bandwidthTimeseries;

    @JsonProperty("visits")
    private List<Visit> visits;

}
