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
@ApiModel(value = "WAF Dn", description = "WAF Dn Object")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonPropertyOrder({
        "dns_record_name",
        "set_type_to",
        "set_data_to"
})
public class Dn implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("dns_record_name")
    private String dnsRecordName;

    @JsonProperty("set_type_to")
    private String setTypeTo;

    @JsonProperty("set_data_to")
    private List<String> setDataTo;

}
