package com.saintgobain.dsi.website4sg.core.domain.deployment;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "DEPLOYMENTTYPE")
public class DeploymentTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DETY_ID")
    private String deploymentTypeId;

    @Column(name = "DETY_LABEL")
    private String label;

    @Column(name = "DETY_ENABLE")
    private Boolean enable;

    @OneToMany(mappedBy = "deploymentType", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DeploymentEntity> deploymentByDeploymentType;
}