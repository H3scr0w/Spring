package com.saintgobain.dsi.pcpeg.security;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.config.PcpegPropertiesResolver;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessControl {
    private final CompanyRepository companyRepository;
    private final PcpegPropertiesResolver propertiesResolver;

    public boolean isAdmin(final Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(right -> right.getAuthority().equals("ROLE_"
                + propertiesResolver
                        .getSuperAdmin()) || right.getAuthority().equals("ROLE_" + propertiesResolver.getAdmin()));
    }

    @Transactional(readOnly = true)
    public PeDimSociete checkAccessCompanyId(Authentication authentication, Integer societeSid) {
        PeDimSociete result = null;
        if (isAdmin(authentication)) {
            result = companyRepository.findBySocieteSid(societeSid)
                    .orElseThrow(() -> new EntityNotFoundException("Company not found with id:" + societeSid));
        } else {
            result = companyRepository.findBySocieteSidAndPeParSocietes_PeDimUtilisateurs_SgidEquals(societeSid,
                    authentication.getPrincipal().toString()).orElseThrow(() -> new EntityNotFoundException(
                            "You don't have rights or company not found with id:"
                                    + societeSid));
        }
        return result;
    }
}
