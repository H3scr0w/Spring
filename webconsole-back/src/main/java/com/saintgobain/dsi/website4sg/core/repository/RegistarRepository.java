package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.RegistarEntity;

public interface RegistarRepository extends JpaRepository<RegistarEntity, Long>,
        JpaSpecificationExecutor<RegistarEntity> {
    Optional<RegistarEntity> findByCode(String code);
}
