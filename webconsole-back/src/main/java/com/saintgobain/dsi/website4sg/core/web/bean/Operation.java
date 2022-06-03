package com.saintgobain.dsi.website4sg.core.web.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operation;
    private String field;
    private String value;

}
