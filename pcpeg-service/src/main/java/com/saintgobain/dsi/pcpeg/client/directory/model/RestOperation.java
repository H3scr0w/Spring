package com.saintgobain.dsi.pcpeg.client.directory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "RestOperation", description = "Rest operation")
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = JsonInclude.Include.NON_NULL)
public class RestOperation {
    @JsonProperty(value = "operation")
    @ApiModelProperty(value = "Rest operation")
    private String operation;

    @JsonProperty(value = "field")
    @ApiModelProperty(value = "Object field")
    private String field;

    @JsonProperty(value = "value")
    @ApiModelProperty(value = "Object new value")
    private Object value;
}
