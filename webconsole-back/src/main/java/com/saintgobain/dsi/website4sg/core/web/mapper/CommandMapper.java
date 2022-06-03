package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentCommandEntity;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Command;
import com.saintgobain.dsi.website4sg.core.web.bean.CommandHeader;

public class CommandMapper {

    public static DeploymentCommandEntity toCommandVO(Command command, DeploymentEntity deploymentEntity) {
        if (command == null) {
            return null;
        }
        return DeploymentCommandEntity.builder()
                .command(command.getCommand() + " " + command.getParam())
                .order(command.getOrder())
                .deployment(deploymentEntity)
                .build();
    }

    public static List<DeploymentCommandEntity> toCommandVOList(List<Command> commands,
            DeploymentEntity deploymentEntity) {
        return commands.stream()
                .map(command -> toCommandVO(command, deploymentEntity))
                .collect(Collectors.toList());
    }

    public static Command toCommand(DeploymentCommandEntity deploymentCommandEntity) {
        if (deploymentCommandEntity == null) {
            return null;
        }
        return Command.builder()
                .command(deploymentCommandEntity.getCommand())
                .order(deploymentCommandEntity.getOrder())
                .build();
    }

    public static List<Command> toCommandList(List<DeploymentCommandEntity> deploymentCommandEntities) {
        return deploymentCommandEntities.stream()
                .map(deploymentCommandEntity -> toCommand(deploymentCommandEntity))
                .collect(Collectors.toList());
    }

    public static CommandHeader toCommandHeader(DeploymentCommandEntity deploymentCommandEntity) {
        if (deploymentCommandEntity == null) {
            return null;
        }
        return CommandHeader.builder()
                .command(deploymentCommandEntity.getCommand())
                .order(deploymentCommandEntity.getOrder())
                .build();
    }

    public static List<CommandHeader> toCommandHeaderList(List<DeploymentCommandEntity> deploymentCommandEntities) {
        return deploymentCommandEntities.stream()
                .map(deploymentCommandEntity -> toCommandHeader(deploymentCommandEntity))
                .collect(Collectors.toList());
    }

}
