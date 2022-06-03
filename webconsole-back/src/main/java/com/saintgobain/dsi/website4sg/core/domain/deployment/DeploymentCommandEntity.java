package com.saintgobain.dsi.website4sg.core.domain.deployment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "DEPLOYMENTCOMMAND")
public class DeploymentCommandEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DCMD_ID")
    private Long deploymentCommandId;

    @Column(name = "DCMD_ORDER")
    private Integer order;

    @Column(name = "DCMD_COMMAND")
    private String command;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPL_ID", nullable = false)
    @ToString.Exclude
    private DeploymentEntity deployment;

    @Column(name = "DEPL_ID", insertable = false, updatable = false)
    private Long deploymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPS_ID", nullable = false)
    @ToString.Exclude
    private DeploymentStepEntity deploymentStep;

    @Column(name = "DEPS_ID", insertable = false, updatable = false)
    private String deploymentStepId;

}