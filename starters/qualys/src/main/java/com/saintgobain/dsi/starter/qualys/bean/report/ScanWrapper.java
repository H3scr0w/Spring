package com.saintgobain.dsi.starter.qualys.bean.report;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScan;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ScanWrapper", description = "ScanWrapper Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("WasScan")
    private WasScan scan;

}
