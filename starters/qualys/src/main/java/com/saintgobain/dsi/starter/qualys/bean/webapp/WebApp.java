package com.saintgobain.dsi.starter.qualys.bean.webapp;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.bean.RobotType;
import com.saintgobain.dsi.starter.qualys.bean.ScannerAppliance;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "WebApp", description = "WebApp Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebApp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String url;

    private AuthRecordsWrapper authRecords;

    private ScannerAppliance defaultScanner;

    private OptionProfile defaultProfile;

    private RobotType useRobots;

    private Boolean useSitemap;

    private Date createdDate;

    private Date updatedDate;

}
