package com.saintgobain.dsi.pcpeg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;

public interface PeDimAnneeRepository extends JpaRepository<PeDimAnnee,Integer>, JpaSpecificationExecutor<PeDimAnnee> {
    Optional<PeDimAnnee> findByAnneeId(Short year);
    Optional<PeDimAnnee> findByFlagEnCours(Boolean flag);

    Optional<PeDimAnnee> findTop1ByOrderByAnneeIdDesc();
    List<PeDimAnnee> findTop2ByOrderByAnneeIdDesc();
}
