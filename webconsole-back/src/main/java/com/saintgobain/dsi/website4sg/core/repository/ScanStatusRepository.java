package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.scan.ScanStatusEntity;

public interface ScanStatusRepository extends JpaRepository<ScanStatusEntity, String> {

}
