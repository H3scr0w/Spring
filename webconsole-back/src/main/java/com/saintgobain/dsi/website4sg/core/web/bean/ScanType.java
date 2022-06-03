package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ScanType", description = "ScanType Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanType implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String scanTypeId;

}
