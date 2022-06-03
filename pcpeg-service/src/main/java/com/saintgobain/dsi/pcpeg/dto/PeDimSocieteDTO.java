package com.saintgobain.dsi.pcpeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PeDimSocieteDTO", description = "PeDimSociete DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeDimSocieteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int societeSid;

    private String societeLibelle;

    private String codeSif;

    private String codeAmundi;

    private Boolean flagAdherente;

    private String comments;

    @Size(max = 3)
    private String cspId;

}
