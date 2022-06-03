package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.pcpeg.domain.PeDimGrpFonds;

public interface PeDimGrpFondsRepository extends JpaRepository<PeDimGrpFonds, String> {

    Optional<PeDimGrpFonds> findByGrpFondsIdStartingWithIgnoreCase(String groupId);

}
