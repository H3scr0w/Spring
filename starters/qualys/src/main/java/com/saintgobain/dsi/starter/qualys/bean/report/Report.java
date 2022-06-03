package com.saintgobain.dsi.starter.qualys.bean.report;

import java.io.Serializable;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saintgobain.dsi.starter.qualys.bean.User;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "Report", description = "Report Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    private String description;

    private User owner;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final ReportType type = ReportType.WAS_SCAN_REPORT;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final ReportFormat format = ReportFormat.PDF;

    private String status;

    private Long size;

    private Date creationDate;

    private Date lastDownloadDate;

    private Integer downloadCount;

    @NotNull
    @Valid
    private ConfigReport config;

}
