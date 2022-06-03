package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;

public interface PeDimUtilisateursRepository extends JpaRepository<PeDimUtilisateurs, Short>,
        JpaSpecificationExecutor<PeDimUtilisateurs> {

    Optional<PeDimUtilisateurs> findFirstBySgid(String sgid);
}
