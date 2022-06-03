package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;

public interface CmsRepository extends JpaRepository<CmsEntity, Long>, JpaSpecificationExecutor<CmsEntity> {

    Optional<CmsEntity> findByCode(String code);
}
