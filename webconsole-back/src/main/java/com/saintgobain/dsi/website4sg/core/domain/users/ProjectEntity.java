package com.saintgobain.dsi.website4sg.core.domain.users;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.WebsiteEntity;
import com.saintgobain.dsi.website4sg.core.domain.scan.ScanEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECT")
public class ProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJ_ID")
    private Long projectId;

    @OneToOne()
    @ToString.Exclude
    private DrupalDocrootCoreEntity drupaldocrootcore;

    @OneToOne()
    @ToString.Exclude
    private WebsiteEntity website;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DeploymentEntity> deployment;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AccessRightEntity> accessrightByProject;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ScanEntity> scanByProject;

}