package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

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
@Builder(toBuilder = true)
@ApiModel(value = "LDAPUser", description = "LDAPUser Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LDAPUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;

    private String cn;

    private String sn;

    private String wsipcompany;

    private String wsippublickey;

    private String lastlogintime;

}
