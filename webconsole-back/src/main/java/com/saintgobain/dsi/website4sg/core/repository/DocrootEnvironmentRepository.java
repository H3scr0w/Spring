package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;

public interface DocrootEnvironmentRepository extends JpaRepository<DocrootEnvironmentEntity, Long>,
        JpaSpecificationExecutor<DocrootEnvironmentEntity> {

    Optional<DocrootEnvironmentEntity> findByDocrootIdAndEnvironmentId(Long docrootId, Long environmentId);

}
