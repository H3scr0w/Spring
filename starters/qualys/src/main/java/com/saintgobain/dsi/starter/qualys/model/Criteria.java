package com.saintgobain.dsi.starter.qualys.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Criteria", description = "Criteria Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Criteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private String field;

    private String operator;

    private String value;

}
