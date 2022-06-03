package com.saintgobain.dsi.pcpeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.pcpeg.domain.PeParSuiviUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviUtilisateursId;

public interface PeParSuiviUtilisateursRepository extends
        JpaRepository<PeParSuiviUtilisateurs, PeParSuiviUtilisateursId> {

}
