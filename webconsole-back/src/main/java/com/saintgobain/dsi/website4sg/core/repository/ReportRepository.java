package com.saintgobain.dsi.website4sg.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.scan.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

}
