package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;

public interface HostingProviderRepository extends JpaRepository<HostingProviderEntity, Long>,
        JpaSpecificationExecutor<HostingProviderEntity> {

    Optional<HostingProviderEntity> findByCode(String code);

}
