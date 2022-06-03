package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.users.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    Optional<RolesEntity> findByLabel(String label);
}
