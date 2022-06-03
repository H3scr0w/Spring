package com.saintgobain.dsi.starter.qualys.bean.webapp;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.saintgobain.dsi.starter.qualys.bean.SetWrapper;
import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthRecord;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "AuthRecordsWrapper", description = "AuthRecordsWrapper Object")
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRecordsWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private SetWrapper<WebAppAuthRecord> set;

}
