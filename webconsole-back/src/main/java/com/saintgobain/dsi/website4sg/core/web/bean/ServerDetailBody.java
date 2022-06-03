package com.saintgobain.dsi.website4sg.core.web.bean;

import java.util.Date;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ServerDetailBody", description = "ServerDetailBody Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerDetailBody {

    @NotNull
    private String domain;

    private Boolean enable;

    private Boolean sshServer;

    private String login;

    @NotNull
    private String hostname;

    private Date created;

    private Date lastUpdate;

}