package com.saintgobain.dsi.starter.qualys.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Filters", description = "Filters Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Filters implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "Criteria")
    private List<Criteria> criteria;

}
