package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;

public interface AccessRightsRepository extends JpaRepository<AccessRightEntity, Long>,
        JpaSpecificationExecutor<AccessRightEntity> {

    Optional<AccessRightEntity> findByProjectIdAndRoleIdAndUserId(Long projectId, Long roleId, Long userId);
}
