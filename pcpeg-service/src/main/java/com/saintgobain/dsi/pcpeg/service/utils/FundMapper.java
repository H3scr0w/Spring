package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.function.Function;

import org.thymeleaf.util.StringUtils;

import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.domain.PeDimContactFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimGrpFonds;
import com.saintgobain.dsi.pcpeg.dto.ContactDTO;
import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.dto.TenantAccountDTO;

public class FundMapper implements Function<PeDimFonds, FundDTO> {

    @Override
    public FundDTO apply(PeDimFonds fundEntity) {

        ContactDTO contactDto = null;
        if (fundEntity.getPeDimContactFonds() != null) {
            PeDimContactFonds contactEntity = fundEntity.getPeDimContactFonds();
            contactDto = ContactDTO
                    .builder()
                    .email(contactEntity.getEmail())
                    .id(contactEntity.getContactId())
                    .name(contactEntity.getNomContact())
                    .phone(contactEntity.getTelephone())
                    .build();
        }
        
        PeDimGrpFonds peDimGrpFonds = fundEntity.getPeDimGrpFonds();

        return FundDTO
                .builder()
                .tenantAccount(fundEntity.getPeRefTeneurCompte() != null ? TenantAccountDTO
                        .builder()
                        .teneurCompteId(fundEntity.getPeRefTeneurCompte().getTeneurCompteId())
                        .teneurCompteLibelle(fundEntity.getPeRefTeneurCompte().getTeneurCompteLibelle())
                        .build() : null)
                .amundiCode(fundEntity.getCodeFondsAmundi())
                .contact(contactDto)
                .fundId(fundEntity.getFondsSid())
                .fundLabel(fundEntity.getFondsLibelle())
                .isActive(fundEntity.getFlagEnCoursCreation())
                .isDefault(peDimGrpFonds != null ? !StringUtils.equalsIgnoreCase(peDimGrpFonds.getGrpFondsId(),
                        Constants.FUND_SPECIFIQUE) : null)
                .fundGroupId(peDimGrpFonds != null ? peDimGrpFonds.getGrpFondsId() : null)
                .build();
    }

}
