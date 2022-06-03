package com.saintgobain.dsi.pcpeg.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Fund DTO related to PeDimFonds Entity.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FundDTO", description = "Fund DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
public class FundDTO {

    private Short fundId;

    @NotBlank
    private String fundLabel;

    @NotBlank
    private String amundiCode;

    private Boolean isDefault;

    private Boolean isActive;

    // PeDimContactFonds
    private ContactDTO contact;

    // PeRefTeneurCompte
    private TenantAccountDTO tenantAccount;

    @NotBlank
    // PeDimGrpFonds
    private String fundGroupId;

}
