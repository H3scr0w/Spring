package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissementId;

public interface FacilityRepository extends JpaRepository<PeDimEtablissement, PeDimEtablissementId>,
        JpaSpecificationExecutor<PeDimEtablissement> {

    Optional<PeDimEtablissement> findById_FacilityIdAndId_SocieteSid(String facilityId, Integer societeId);

}
