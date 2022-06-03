package com.saintgobain.dsi.starter.incapsula.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.saintgobain.dsi.starter.incapsula.bean.Continents;
import com.saintgobain.dsi.starter.incapsula.bean.Countries;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WAF Geo", description = "WAF Geo Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "continents",
        "countries" })
public class Geo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("continents")
    private List<Continents> continents;

    @JsonProperty("countries")
    private List<Countries> countries;

}
