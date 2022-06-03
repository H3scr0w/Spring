package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment.DeploymentBuilder;
import com.saintgobain.dsi.website4sg.core.web.bean.DeploymentStatus;
import com.saintgobain.dsi.website4sg.core.web.bean.DeploymentType;

public class DeploymentMapper {

    public static Deployment toDeployment(DeploymentEntity deploymentEntity, boolean isAdmin) {
        if (deploymentEntity == null) {
            return null;
        }

        DeploymentBuilder builder = Deployment.builder()
                .deploymentId(deploymentEntity.getDeploymentId())
                .docrootCode(deploymentEntity.getDocrootCode())
                .environmentCode(deploymentEntity.getEnvironmentCode())
                .deliverableCode(deploymentEntity.getDeliverableCode())
                .deliverableVersion(deploymentEntity.getDeliverableVersion())
                .requester(deploymentEntity.getRequester())
                .creationDate(deploymentEntity.getCreated());

        if (isAdmin) {
            builder.rundeckJobId(deploymentEntity.getRundeckJobId());
        }

        Deployment deployment = builder.build();

        if (deploymentEntity.getDeploymentStatus() != null) {
            deployment.setDeploymentStatus(DeploymentStatus.builder()
                    .deploymentStatusId(deploymentEntity.getDeploymentStatus().getDeploymentStatusId())
                    .label(deploymentEntity.getDeploymentStatus().getLabel())
                    .build());
        }

        if (deploymentEntity.getDeploymentType() != null) {
            deployment.setDeploymentType(DeploymentType.builder()
                    .deploymentTypeId(deploymentEntity.getDeploymentType().getDeploymentTypeId())
                    .label(deploymentEntity.getDeploymentType().getLabel())
                    .build());
        }

        return deployment;
    }

    public static List<Deployment> toDeploymentList(List<DeploymentEntity> deploymentEntities, boolean isAdmin) {
        return deploymentEntities.stream().map(deployment -> toDeployment(deployment, isAdmin)).collect(Collectors
                .toList());
    }

    public static Page<Deployment> toDeploymentPageableList(Page<DeploymentEntity> deploymentEntities,
            boolean isAdmin) {
        List<Deployment> deploymentBodies = deploymentEntities.getContent().stream().map(deployment -> toDeployment(
                deployment, isAdmin)).collect(Collectors
                        .toList());
        return new PageImpl<Deployment>(deploymentBodies, deploymentEntities.getPageable(), deploymentEntities
                .getTotalElements());
    }
}
