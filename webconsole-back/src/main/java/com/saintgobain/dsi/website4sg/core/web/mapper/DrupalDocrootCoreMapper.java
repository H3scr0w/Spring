package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCore;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreBody;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreBody.DrupalDocrootCoreBodyBuilder;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreHeader;

public class DrupalDocrootCoreMapper {
    public static DrupalDocrootCore toDrupalDocrootCore(DrupalDocrootCoreEntity drupalDocrootCoreEntity) {
        if (drupalDocrootCoreEntity == null) {
            return null;
        }
        return DrupalDocrootCore.builder().codeRepositoryUrl(drupalDocrootCoreEntity.getCodeRepositoryUrl()).build();
    }

    public static DrupalDocrootCoreHeader toDrupalDocrootCoreHeader(DrupalDocrootCoreEntity drupalDocrootCoreEntity) {
        if (drupalDocrootCoreEntity == null) {
            return null;
        }
        return DrupalDocrootCoreHeader.builder()
                .code(drupalDocrootCoreEntity.getCode())
                .name(drupalDocrootCoreEntity.getName())
                .build();
    }

    public static List<DrupalDocrootCoreHeader> toDrupalDocrootCoreHeaderList(
            List<DrupalDocrootCoreEntity> drupalDocrootCoreEntities) {
        return drupalDocrootCoreEntities.stream().map(drupalDocrootCoreEntity -> toDrupalDocrootCoreHeader(
                drupalDocrootCoreEntity)).collect(Collectors.toList());
    }

    public static DrupalDocrootCoreBody toDrupalDocrootCoreBody(DrupalDocrootCoreEntity drupalDocrootCoreEntity,
            boolean isAdmin) {
        if (drupalDocrootCoreEntity == null) {
            return null;
        }

        DrupalDocrootCoreBodyBuilder builder = DrupalDocrootCoreBody.builder()
                .code(drupalDocrootCoreEntity.getCode())
                .name(drupalDocrootCoreEntity.getName())
                .codeRepositoryUrl(drupalDocrootCoreEntity.getCodeRepositoryUrl());

        if (isAdmin) {
            builder.binaryRepositoryUrl(drupalDocrootCoreEntity.getBinaryRepositoryUrl());
        }

        return builder.build();
    }

    public static Page<DrupalDocrootCoreBody> toDocrootCoreBodyPage(Page<DrupalDocrootCoreEntity> docrootCoreEntities,
            boolean isAdmin) {
        List<DrupalDocrootCoreBody> docrootCoreBodies = docrootCoreEntities.getContent()
                .stream()
                .map(docrootCore -> toDrupalDocrootCoreBody(docrootCore, isAdmin))
                .collect(Collectors.toList());
        return new PageImpl<DrupalDocrootCoreBody>(docrootCoreBodies, docrootCoreEntities.getPageable(),
                docrootCoreEntities.getTotalElements());
    }

    public static DrupalDocrootCoreEntity toDrupalDocrootCoreEntity(DrupalDocrootCore drupalDocrootCore, String code) {
        if (drupalDocrootCore == null) {
            return null;
        }
        return DrupalDocrootCoreEntity.builder()
                .code(code)
                .name(drupalDocrootCore.getName())
                .codeRepositoryUrl(drupalDocrootCore.getCodeRepositoryUrl())
                .binaryRepositoryUrl(drupalDocrootCore.getBinaryRepositoryUrl())
                .build();
    }

}
