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
@ApiModel(value = "WAF GeneratedCertificate", description = "WAF GeneratedCertificate Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "ca",
        "validation_method",
        "validation_data",
        "san",
        "validation_status"
})
public class GeneratedCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("ca")
    private String ca;

    @JsonProperty("validation_method")
    private String validationMethod;

    @JsonProperty("validation_data")
    private String validationData;

    @JsonProperty("san")
    private List<String> san;

    @JsonProperty("validation_status")
    private String validationStatus;

}
