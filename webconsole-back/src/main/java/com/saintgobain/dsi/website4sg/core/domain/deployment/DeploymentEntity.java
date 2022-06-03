package com.saintgobain.dsi.website4sg.core.domain.deployment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "DEPLOYMENT")
public class DeploymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPL_ID")
    private Long deploymentId;

    @Column(name = "DEPL_DOCROOT_CODE")
    private String docrootCode;

    @Column(name = "DEPL_ENVIRONMENT_CODE")
    private String environmentCode;

    @Column(name = "DEPL_DELIVERABLE_CODE")
    private String deliverableCode;

    @Column(name = "DEPL_DELIVERABLE_VERSION")
    private String deliverableVersion;

    @Column(name = "DEPL_REQUESTER")
    private String requester;

    @Column(name = "DEPL_RUNDECK_JOBID")
    private String rundeckJobId;

    @Column(name = "DEPL_CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "DEPL_LASTUPDATE")
    @Temporal(TemporalType.DATE)
    private Date updated;

    @Column(name = "DEPL_VALIDATEDBY")
    private String validatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEST_ID", nullable = false)
    @ToString.Exclude
    private DeploymentStatusEntity deploymentStatus;

    @Column(name = "DEST_ID", insertable = false, updatable = false)
    private String deploymentStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DETY_ID", nullable = false)
    @ToString.Exclude
    private DeploymentTypeEntity deploymentType;

    @Column(name = "DETY_ID", insertable = false, updatable = false)
    private String deploymentTypeId;

    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DeploymentCommandEntity> deploymentCommand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJ_ID", nullable = false)
    @ToString.Exclude
    private ProjectEntity project;

    @Column(name = "PROJ_ID", insertable = false, updatable = false)
    private Long projectId;

}