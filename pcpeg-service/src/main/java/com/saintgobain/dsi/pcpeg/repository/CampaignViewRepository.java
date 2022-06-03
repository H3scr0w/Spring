package com.saintgobain.dsi.pcpeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.CampaignView;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;

public interface CampaignViewRepository extends JpaRepository<CampaignView, PeParSuiviSocietesId>,
        JpaSpecificationExecutor<CampaignView> {

    long countByFlagEnCoursTrueAndId_FormulaireId(Short formulaireId);

    long countByFlagEnCoursTrueAndStatutIdAndId_FormulaireId(Short statutId, Short formulaireId);

    long countById_FormulaireIdAndId_AnneeId(Short formulaireId, Short year);

    long countByStatutIdAndId_FormulaireIdAndId_AnneeId(Short statutId, Short formulaireId,
            Short year);

}
