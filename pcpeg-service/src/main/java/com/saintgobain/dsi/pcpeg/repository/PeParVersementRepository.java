package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.pcpeg.domain.PeParVersement;

public interface PeParVersementRepository extends JpaRepository<PeParVersement, Integer> {

    Optional<PeParVersement> findFirstByPeDimTypeVersement_TypeVersementSidAndPeParSociete_Id_SocieteSidAndPeParSociete_Id_AnneeId(
            Integer paymentType, Integer societeSid, Short year);

}
