package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.scan.ScanTypeEntity;

public interface ScanTypeRepository extends JpaRepository<ScanTypeEntity, String> {

}
