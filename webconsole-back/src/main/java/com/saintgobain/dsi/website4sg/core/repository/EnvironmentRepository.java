package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;

public interface EnvironmentRepository extends JpaRepository<EnvironmentEntity, Long>,
        JpaSpecificationExecutor<EnvironmentEntity> {

    Optional<EnvironmentEntity> findByCodeIgnoreCase(String code);

    Page<EnvironmentEntity> findAllByNameIgnoreCaseStartingWith(String name, Pageable pageable);

}
