package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByWebsiteCode(String websiteCode);

    Optional<ProjectEntity> findByDrupaldocrootcoreCode(String drupaldocrootcoreCode);

    Page<ProjectEntity> findAllDistinctByAccessrightByProject_Users_EmailEquals(String email, Pageable pageable);

    Page<ProjectEntity> findAll(Specification<ProjectEntity> spec, Pageable pageable);
}
