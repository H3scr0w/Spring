package com.saintgobain.dsi.pcpeg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSocieteId;

public interface PeParFondsAutorisesSocieteRepository extends
        JpaRepository<PeParFondsAutorisesSociete, PeParFondsAutorisesSocieteId>,
        JpaSpecificationExecutor<PeParFondsAutorisesSociete> {

    @EntityGraph(attributePaths = {
            "peParVersement",
            "peParVersement.peDimTypeVersement",
            "peParVersement.peParSociete" })
    List<PeParFondsAutorisesSociete> findByPeParVersement_PeDimTypeVersement_TypeVersementSidAndPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeId(
            Integer paymentType, Integer societeId, Short year);

}
