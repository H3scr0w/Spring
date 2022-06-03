package com.saintgobain.dsi.website4sg.core.web.bean;

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
@ApiModel(value = "Scan", description = "Scan Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scan implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String scanToolId;

    @NotNull
    private String refId1;

    private String refId2;

    private String refId3;

    @NotNull
    private String reportToolId;

    @NotNull
    private ScanType scanType;

    private ScanStatus scanStatus;

}
