package com.saintgobain.dsi.starter.incapsula.bean;

import java.util.List;

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
@ApiModel(value = "TrafficVisit", description = "TrafficVisit Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class TrafficVisit {

    private TimeRanges timeRange;

    private List<? extends Securities> securities;

    private List<String> countries;

    private List<String> ips;

    private List<String> visitIds;

    private Boolean listLiveVisits;

}
