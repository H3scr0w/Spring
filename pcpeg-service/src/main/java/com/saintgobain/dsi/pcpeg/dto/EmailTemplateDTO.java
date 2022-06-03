package com.saintgobain.dsi.pcpeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EmailTemplateDTO", description = "EmailTemplate DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = JsonInclude.Include.NON_NULL)
public class EmailTemplateDTO {

    @NotBlank
    private String objetInitial;

    @NotBlank
    private String objetRelance;

    @NotBlank
    private String mailInitial;

    @NotBlank
    private String mailRelance;

    @NotBlank
    private Date formulaireDateLimiteReponse;
}
