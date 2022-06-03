package com.saintgobain.dsi.starter.qualys.bean.wasscan;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value = "WasScan", description = "WasScan Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WasScan implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    private String reference;

    @NotNull
    private WasScanType type;

    private WasScanMode mode;

    private WasScanStatus status;

    @NotNull
    private WasScanTarget target;

    @NotNull
    private WasScanOptionProfile profile;

    private String cancelTime;

    private User launchedBy;

    private Date launchedDate;

    private Date endScanDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Boolean sendMail = false;

}
