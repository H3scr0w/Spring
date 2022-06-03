package com.saintgobain.dsi.pcpeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Payment DTO related to PeParVersement Entity.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PaymentDTO", description = "Payment DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class PaymentDTO {

    private Integer paymentId;

    // PeParSociete
    private Integer companyId;

    // PeParSociete
    private Short year;

    // PeDimTypeVersement
    private Integer paymentType;

    @Builder.Default
    private Boolean flagVersement = false;

    @Builder.Default
    private Boolean flagVersementInfra = false;

    @Builder.Default
    private Boolean flagVersementBlocPlie = false;

}
