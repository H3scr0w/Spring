package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

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
@ApiModel(value = "CmsHeader", description = "CmsHeader Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;
}
