package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.referential.DomainTypeEntity;

public interface DomainTypeRepository extends JpaRepository<DomainTypeEntity, String> {

    Optional<DomainTypeEntity> findByDomainTypeIdIgnoreCase(String domainType);

}
