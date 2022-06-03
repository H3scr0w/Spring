package com.saintgobain.dsi.website4sg.core.domain.scan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCAN")
public class ScanEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCAN_ID")
    private Long scanId;

    @Column(name = "SCAN_TOOL_ID", unique = true)
    private String scanToolId;

    @Column(name = "REF_ID1")
    private String refId1;

    @Column(name = "REF_ID2")
    private String refId2;

    @Column(name = "REF_ID3")
    private String refId3;

    @Column(name = "REPORT_TOOL_ID", unique = true)
    private String reportToolId;

    @OneToOne(mappedBy = "scan", fetch = FetchType.LAZY)
    @ToString.Exclude
    private ReportEntity report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJ_ID", nullable = false)
    @ToString.Exclude
    private ProjectEntity project;

    @Column(name = "PROJ_ID", insertable = false, updatable = false)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCST_ID", nullable = false)
    @ToString.Exclude
    private ScanStatusEntity scanStatus;

    @Column(name = "SCST_ID", insertable = false, updatable = false)
    private String scanStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCTY_ID", nullable = false)
    @ToString.Exclude
    private ScanTypeEntity scanType;

    @Column(name = "SCTY_ID", insertable = false, updatable = false)
    private String scanTypeId;

    @Column(name = "SCAN_CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

}
