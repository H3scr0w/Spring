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
@ApiModel(value = "WAF Ssl", description = "WAF Ssl Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "origin_server",
        "generated_certificate",
        "custom_certificate"
})
public class Ssl implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("origin_server")
    private OriginServer originServer;

    @JsonProperty("generated_certificate")
    private GeneratedCertificate generatedCertificate;

    @JsonProperty("custom_certificate")
    private CustomCertificate customCertificate;

}
