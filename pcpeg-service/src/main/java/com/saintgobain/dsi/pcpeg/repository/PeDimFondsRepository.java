package com.saintgobain.dsi.pcpeg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;

public interface PeDimFondsRepository extends JpaRepository<PeDimFonds, Short>, JpaSpecificationExecutor<PeDimFonds> {

    @EntityGraph(attributePaths = {
            "peDimGrpFonds" })
    List<PeDimFonds> findDistinctByPeDimGrpFonds_GrpFondsIdStartsWithIgnoreCase(String fundGroup);

    Optional<PeDimFonds> findDistinctByFondsLibelleContainingIgnoreCase(String fundLabel);

    Optional<PeDimFonds> findDistinctByFondsLibelleIgnoreCaseAndCodeFondsAmundiIgnoreCaseAndPeRefTeneurCompte_TeneurCompteId(String fundLabel, String amundiCode, Short accountId);

}
