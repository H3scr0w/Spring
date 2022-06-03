package com.saintgobain.dsi.pcpeg.client.directory.model;

import java.util.List;

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
@ApiModel(value = "Group", description = "Group detail")
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = JsonInclude.Include.NON_NULL)
public class Group {
    @JsonProperty(value = "_id")
    @ApiModelProperty(value = "Group id")
    private String id;

    @JsonProperty(value = "cn")
    @ApiModelProperty(value = "Group cn")
    private String cn;

    @JsonProperty(value = "uniqueMember")
    @ApiModelProperty(value = "Group uniqueMember")
    private List<String> uniqueMember;
}
