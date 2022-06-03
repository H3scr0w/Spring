package com.saintgobain.dsi.pcpeg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.pcpeg.domain.PeParAccords;

public interface PeParAccordsRepository extends JpaRepository<PeParAccords, Integer> {

    List<PeParAccords> findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeIdAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
            Integer societeId, Short year, Integer paymentType);

    List<PeParAccords> findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
            Integer societeId, Integer paymentType);

}
