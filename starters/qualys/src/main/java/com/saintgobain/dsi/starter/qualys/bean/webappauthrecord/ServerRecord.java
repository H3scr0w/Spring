package com.saintgobain.dsi.starter.qualys.bean.webappauthrecord;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.saintgobain.dsi.starter.qualys.bean.Fields;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ServerRecord", description = "ServerRecord Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    @NotNull
    private Boolean sslOnly;

    @NotNull
    private Fields<WebAppAuthServerRecordField> fields;

}
