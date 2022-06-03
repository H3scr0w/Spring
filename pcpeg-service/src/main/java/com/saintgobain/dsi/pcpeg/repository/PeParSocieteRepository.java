package com.saintgobain.dsi.pcpeg.repository;

import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSocieteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PeParSocieteRepository extends JpaRepository<PeParSociete, PeParSocieteId>, JpaSpecificationExecutor<PeParSociete> {

    Optional<PeParSociete> findFirstById_SocieteSidAndId_AnneeId(Integer societeId, Short year);

    List<PeParSociete> findById_AnneeId(Short year);

    List<PeParSociete> findAllById_AnneeIdAndId_SocieteSidIn(Short year,List<Integer> societeIds);
}
