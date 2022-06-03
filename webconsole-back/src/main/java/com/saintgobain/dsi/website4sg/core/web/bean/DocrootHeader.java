package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "DocrootHeader", description = "DocrootHeader Object", subTypes = {
        DocrootBody.class })
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocrootHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    protected String code;

    protected String name;

    @NotNull
    @ApiModelProperty(hidden = true)
    protected String rundeckJobApiUrl;

    protected String providerInternalId;

    @NotNull
    protected String hostingProviderCode;

    protected String hostingProviderName;

}