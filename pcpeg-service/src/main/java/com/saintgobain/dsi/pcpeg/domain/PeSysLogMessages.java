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
@Table(name = "PE_SYS_LOG_MESSAGES")
public class PeSysLogMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LOG_SID", unique = true, nullable = false, precision = 22, scale = 0)
    private BigDecimal logSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODE")
    private PeSysRefCodeMessage peSysRefCodeMessage;

    @Column(name = "SYS_DATE_INSERTION", nullable = false)
    private Serializable sysDateInsertion;

    @Column(name = "PID", length = 20)
    private String pid;

    @Column(name = "ROOT_PID", length = 20)
    private String rootPid;

    @Column(name = "FATHER_PID", length = 20)
    private String fatherPid;

    @Column(name = "PROJECT", length = 50)
    private String project;

    @Column(name = "JOB")
    private String job;

    @Column(name = "CONTEXT", length = 50)
    private String context;

    @Column(name = "PRIORITY", precision = 2, scale = 0)
    private Byte priority;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "STATUS", length = 10)
    private String status;

}
