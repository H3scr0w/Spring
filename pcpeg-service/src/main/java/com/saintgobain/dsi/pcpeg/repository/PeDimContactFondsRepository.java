package com.saintgobain.dsi.pcpeg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.pcpeg.domain.PeDimContactFonds;

public interface PeDimContactFondsRepository extends JpaRepository<PeDimContactFonds, Short> {

    Optional<PeDimContactFonds> findFirstByNomContactIgnoreCaseAndEmailIgnoreCase(String name, String email);

    Optional<PeDimContactFonds> findFirstByNomContactIgnoreCase(String name);

    Optional<PeDimContactFonds> findFirstByEmailIgnoreCase(String email);

}
