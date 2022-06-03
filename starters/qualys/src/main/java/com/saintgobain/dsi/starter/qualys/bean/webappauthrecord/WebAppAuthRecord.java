package com.saintgobain.dsi.starter.qualys.bean.webappauthrecord;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@ApiModel(value = "WebAppAuthRecord", description = "WebAppAuthRecord Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebAppAuthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private ServerRecord serverRecord;

    private User owner;

    private Date createdDate;

    private Date updatedDate;

}
