package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;

public interface DocrootRepository extends JpaRepository<DocrootEntity, Long>, JpaSpecificationExecutor<DocrootEntity> {
    Optional<DocrootEntity> findByCode(String code);
}
