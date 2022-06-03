package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.saintgobain.dsi.website4sg.core.domain.referential.ServerEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Server;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerHeader;

public class ServerMapper {

    public static ServerHeader toServerHeader(ServerEntity serverEntity) {
        if (serverEntity == null) {
            return null;
        }
        return ServerHeader.builder().hostname(serverEntity.getHostname()).build();
    }

    public static List<ServerHeader> toServerHeaderList(List<ServerEntity> serverEntities) {
        return serverEntities.stream().map(serverEntity -> toServerHeader(serverEntity)).collect(Collectors.toList());
    }

    public static Page<ServerHeader> toServerHeaderPage(Page<ServerEntity> serverEntities) {
       
        List<ServerHeader> serverHeaders = serverEntities.getContent().stream().map(server -> toServerHeader(server)).collect(Collectors.toList());
        return new PageImpl<ServerHeader>(serverHeaders, serverEntities.getPageable(), serverEntities
                .getTotalElements());
    }

    public static Page<ServerDetailBody> toServerBodyPage(List<ServerEntity> serverEntities, Pageable pageable,
            long totalElement) {
        return new PageImpl<ServerDetailBody>(toServerDetailBodyList(serverEntities), pageable, totalElement);
    }

    public static List<ServerDetailBody> toServerDetailBodyList(List<ServerEntity> serverEntities) {
        return serverEntities.stream().map(serverEntity -> toServerDetailBody(serverEntity)).collect(Collectors
                .toList());
    }

    public static ServerDetailBody toServerDetailBody(ServerEntity serverEntity) {
        return ServerDetailBody.builder()
                .hostname(serverEntity.getHostname())
                .domain(serverEntity.getDomain())
                .enable(serverEntity.getEnable())
                .created(serverEntity.getCreated())
                .lastUpdate(serverEntity.getLastUpdate())
                .sshServer(serverEntity.getSshServer())
                .login(serverEntity.getLogin())
                .build();
    }

    public static ServerEntity toServerEntity(Server server) {
        if (server == null) {
            return null;
        }
        return ServerEntity.builder()
                .domain(server.getDomain())
                .enable(server.getEnable())
                .sshServer(server.getSshServer())
                .login(server.getLogin())
                .build();
    }

}