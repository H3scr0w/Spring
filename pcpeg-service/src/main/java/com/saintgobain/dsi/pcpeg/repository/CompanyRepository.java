package com.saintgobain.dsi.pcpeg.repository;

import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<PeDimSociete, Integer>, JpaSpecificationExecutor<PeDimSociete> {
    Optional<PeDimSociete> findBySocieteSid(Integer id);

    Optional<PeDimSociete> findBySocieteSidAndPeParSocietes_PeDimUtilisateurs_SgidEquals(Integer id, String sgid);

    Optional<PeDimSociete> findFirstByCodeSif(String codeSif);

}
