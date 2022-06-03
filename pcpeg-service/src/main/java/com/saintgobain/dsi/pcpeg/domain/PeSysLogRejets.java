package com.saintgobain.dsi.pcpeg.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_SYS_LOG_REJETS")
public class PeSysLogRejets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LOG_SID", unique = true, nullable = false, precision = 22, scale = 0)
    private BigDecimal logSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODE", nullable = false)
    private PeSysRefCodeRejet peSysRefCodeRejet;

    @Column(name = "SYS_DATE_INSERTION", nullable = false)
    private Serializable sysDateInsertion;

    @Column(name = "TALEND_PROJECT", nullable = false, length = 50)
    private String talendProject;

    @Column(name = "TALEND_JOB", nullable = false)
    private String talendJob;

    @Column(name = "DB_CONNEXION")
    private String dbConnexion;

    @Column(name = "DB_TABLE_CIBLE", length = 50)
    private String dbTableCible;

    @Column(name = "FIC_SOURCE")
    private String ficSource;

    @Column(name = "FIC_NO_LIGNE", precision = 22, scale = 0)
    private BigDecimal ficNoLigne;

    @Column(name = "MESSAGE", length = 1000)
    private String message;

    @Column(name = "TALEND_ERROR_CODE")
    private String talendErrorCode;

    @Column(name = "PID", length = 20)
    private String pid;

    @Column(name = "ROOT_PID", length = 20)
    private String rootPid;

    @Column(name = "FATHER_PID", length = 20)
    private String fatherPid;

}
