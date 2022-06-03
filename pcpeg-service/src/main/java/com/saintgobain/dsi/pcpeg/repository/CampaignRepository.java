package com.saintgobain.dsi.pcpeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;

public interface CampaignRepository extends JpaRepository<PeParSuiviSocietes, PeParSuiviSocietesId>,
        JpaSpecificationExecutor<PeParSuiviSocietes> {
}
