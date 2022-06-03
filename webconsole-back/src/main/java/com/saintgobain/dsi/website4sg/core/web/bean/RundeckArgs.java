package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel(value = "RundeckArgs", description = "RundeckArgs Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RundeckArgs implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String argString;

}
