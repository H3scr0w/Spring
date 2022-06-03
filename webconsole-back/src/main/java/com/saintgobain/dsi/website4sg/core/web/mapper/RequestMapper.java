package com.saintgobain.dsi.website4sg.core.web.mapper;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Request;

public class RequestMapper {

    public static DeploymentEntity toDeploymentEntity(Request request) {
        if (request == null) {
            return null;
        }
        DeploymentEntity deployment = DeploymentEntity.builder()
                .docrootCode(request.getDocrootCode())
                .environmentCode(request.getEnvironmentCode())
                .deliverableCode(request.getDeliverableCode())
                .deliverableVersion(request.getDeliverableVersion())
                .build();
        deployment.setDeploymentCommand(CommandMapper.toCommandVOList(request.getCommands(), deployment));
        return deployment;
    }
}
