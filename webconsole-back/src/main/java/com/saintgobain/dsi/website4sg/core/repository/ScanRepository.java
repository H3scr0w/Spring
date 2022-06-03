package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saintgobain.dsi.website4sg.core.domain.scan.ScanEntity;

public interface ScanRepository extends JpaRepository<ScanEntity, Long> {

    Optional<ScanEntity> findByScanToolId(String scanToolId);

    Optional<ScanEntity> findByReportToolId(String reportToolId);

    Page<ScanEntity> findAllByScanToolIdIsNotNullAndScanTypeIdEquals(String scanTypeId, Pageable pageable);

    Page<ScanEntity> findAllByReportToolIdIsNotNullAndScanTypeIdEquals(String scanTypeId, Pageable pageable);

    Page<ScanEntity> findAllByReportToolIdIsNotNullAndProject_Website_CodeEquals(String websiteCode, Pageable pageable);

    /*** Members Read ***/

    Page<ScanEntity> findAllByReportToolIdIsNotNullAndProject_Website_CodeEqualsAndProject_AccessrightByProject_Roles_LabelEqualsAndProject_AccessrightByProject_Users_EmailEquals(
            String websiteCode, String roleLabel, String email, Pageable pageable);

    /*** Members Read ***/
}
