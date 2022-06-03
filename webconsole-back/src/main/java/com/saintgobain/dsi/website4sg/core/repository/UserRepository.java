package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByEmail(String email);

    Page<UsersEntity> findAllDistinctByAccessrightByUsers_ProjectId(Long projectId, Pageable pageable);

    Page<UsersEntity> findAll(Specification<UsersEntity> spec, Pageable pageable);
}
