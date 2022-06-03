package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentDetail;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentHeader;

public class DocrootEnvironmentMapper {

    public static DocrootEnvironmentDetail toDocrootEnvironmentDetail(
            DocrootEnvironmentEntity docrootEnvironmentEntity) {
        if (docrootEnvironmentEntity == null) {
            return null;
        }
        return DocrootEnvironmentDetail.builder()
                .cmsCode(docrootEnvironmentEntity.getCms().getCode())
                .cmsVersion(docrootEnvironmentEntity.getCmsVersion())
                .drupalDocrootCoreCode(docrootEnvironmentEntity.getDrupaldocrootcore().getCode())
                .drupalDocrootCoreVersion(docrootEnvironmentEntity.getDrupalDocrootCoreVersion())
                .canAutoDeploy(docrootEnvironmentEntity.getCanAutoDeploy())
                .build();
    }

    public static DocrootEnvironmentHeader toDocrootEnvironmentHeader(
            DocrootEnvironmentEntity docrootEnvironmentEntity) {
        if (docrootEnvironmentEntity == null) {
            return null;
        }
        return DocrootEnvironmentHeader.builder()
                .environmentCode(docrootEnvironmentEntity.getEnvironment() != null ? docrootEnvironmentEntity
                        .getEnvironment().getCode().toLowerCase() : null)
                .build();
    }

    public static Page<DocrootEnvironmentHeader> toDocrootEnvironmentHeaderList(
            Page<DocrootEnvironmentEntity> docrootEnvironmentEntities) {
        List<DocrootEnvironmentHeader> docrootEnvironmentBodies = docrootEnvironmentEntities.getContent()
                .stream()
                .map(docrootEnvironment -> toDocrootEnvironmentHeader(docrootEnvironment))
                .collect(Collectors.toList());
        return new PageImpl<DocrootEnvironmentHeader>(docrootEnvironmentBodies, docrootEnvironmentEntities
                .getPageable(), docrootEnvironmentEntities
                        .getTotalElements());

    }

    public static Page<DocrootEnvironmentHeader> fromEnvironmentToDocrootEnvironmentHeaderList(
            Page<EnvironmentEntity> environmentEntities) {
        List<DocrootEnvironmentHeader> docrootEnvironmentBodies = environmentEntities.getContent()
                .stream()
                .map(environment -> DocrootEnvironmentHeader.builder().environmentCode(environment.getCode()
                        .toLowerCase())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<DocrootEnvironmentHeader>(docrootEnvironmentBodies, environmentEntities
                .getPageable(), environmentEntities
                        .getTotalElements());

    }

}
