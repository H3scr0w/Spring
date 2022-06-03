package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.RegistarEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Registar;

public class RegistarMapper {

    public static Registar toRegistar(RegistarEntity registarEntity) {

        if (registarEntity == null) {
            return null;
        }

        return Registar.builder()
                .code(registarEntity.getCode())
                .name(registarEntity.getName())
                .build();
    }

    public static Page<Registar> toRegistarList(Page<RegistarEntity> registarEntities) {
        List<Registar> registarList = registarEntities.getContent()
                .stream()
                .map(registarEntity -> toRegistar(registarEntity))
                .collect(Collectors.toList());

        return new PageImpl<Registar>(registarList, registarEntities.getPageable(), registarEntities
                .getTotalElements());

    }

}
