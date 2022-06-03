package com.saintgobain.dsi.starter.incapsula.model;

import java.io.Serializable;

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
@ApiModel(value = "WAF CustomCertificate", description = "WAF CustomCertificate Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "active",
        "expirationDate",
        "revocationError",
        "validityError",
        "chainError",
        "hostnameMismatchError"
})
public class CustomCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("expirationDate")
    private Long expirationDate;

    @JsonProperty("revocationError")
    private Boolean revocationError;

    @JsonProperty("validityError")
    private Boolean validityError;

    @JsonProperty("chainError")
    private Boolean chainError;

    @JsonProperty("hostnameMismatchError")
    private Boolean hostnameMismatchError;

}
