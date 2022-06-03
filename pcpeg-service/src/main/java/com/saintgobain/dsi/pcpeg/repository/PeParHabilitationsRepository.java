package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;

public interface PeParHabilitationsRepository extends JpaRepository<PeParHabilitations, Short>, JpaSpecificationExecutor<PeParHabilitations> {
    Optional<PeParHabilitations> findFirstByPeParSociete_Id_SocieteSidOrderByPeParSociete_Id_AnneeIdDesc(
            Integer societeSid);

}
