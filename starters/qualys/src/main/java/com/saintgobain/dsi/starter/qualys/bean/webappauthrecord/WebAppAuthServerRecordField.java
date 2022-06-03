package com.saintgobain.dsi.starter.qualys.bean.webappauthrecord;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
@ApiModel(value = "WebAppAuthServerRecordField", description = "WebAppAuthServerRecordField Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAppAuthServerRecordField implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private String domain;

    @NotNull
    private String username;

    private String password;

}
