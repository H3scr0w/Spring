package com.saintgobain.dsi.website4sg.core.web.mapper;

import com.saintgobain.dsi.website4sg.core.domain.referential.EnvironmentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Environment;
import com.saintgobain.dsi.website4sg.core.web.bean.EnvironmentBody;

public class EnvironmentMapper {

    public static EnvironmentBody toEnvironmentBody(EnvironmentEntity environmentEntity) {
        if (environmentEntity == null) {
            return null;
        }
        return EnvironmentBody.builder()
                .code(environmentEntity.getCode().toLowerCase())
                .name(environmentEntity.getName())
                .build();
    }

    public static EnvironmentEntity toEnvironmentEntity(Environment environment, String environmentCode) {
        if (environment == null) {
            return null;
        }
        return EnvironmentEntity.builder()
                .name(environment.getName())
                .code(environmentCode.toLowerCase())
                .build();
    }

}