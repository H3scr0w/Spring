package com.saintgobain.dsi.pcpeg.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Document DTO related to PeParAccords Entity.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DocumentDTO", description = "Document DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class DocumentDTO {

    private Integer documentId;

    private Date startDate;

    private Date endDate;

    private String documentName;

    // PeRefTypeAccord
    private Integer documentType;

    // PeParVersement
    private Integer paymentId;

}
