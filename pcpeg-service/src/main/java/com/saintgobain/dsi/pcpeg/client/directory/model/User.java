package com.saintgobain.dsi.pcpeg.client.directory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class User {

    @JsonProperty("stGoSGI")
    private String sgid;

    @JsonProperty("givenName")
    private String firstName;

    @JsonProperty("sn")
    private String lastName;

    @JsonProperty("cn")
    private String fullName;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("telephoneNumber")
    private String telephoneNumber;

}
