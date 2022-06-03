package com.saintgobain.dsi.pcpeg.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;
import com.saintgobain.dsi.pcpeg.repository.PeRefTeneurCompteRepository;
import com.saintgobain.dsi.pcpeg.service.utils.SortUtil;
import com.saintgobain.dsi.pcpeg.specification.TenantAccountSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantAccountService {

    private final PeRefTeneurCompteRepository tenantAccountRepository;

    @Transactional(readOnly = true)
    public Page<PeRefTeneurCompte> getAllTenantAccounts(Pageable pageable, String label) {
        Specification<PeRefTeneurCompte> spec = Specification.where(null);
        pageable = SortUtil.ignoreCase(pageable);
        pageable = SortUtil.andSort(pageable, "teneurCompteId");

        if (StringUtils.isNotBlank(label)) {
            spec = spec.and(TenantAccountSpecification.labelSpecification(label));
        }

        return tenantAccountRepository.findAll(spec, pageable);

    }

}
