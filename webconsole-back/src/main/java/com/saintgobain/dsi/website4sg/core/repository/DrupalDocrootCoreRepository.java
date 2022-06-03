package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;

public interface DrupalDocrootCoreRepository extends JpaRepository<DrupalDocrootCoreEntity, Long>,
        JpaSpecificationExecutor<DrupalDocrootCoreEntity> {

    Optional<DrupalDocrootCoreEntity> findByCode(String code);

}
