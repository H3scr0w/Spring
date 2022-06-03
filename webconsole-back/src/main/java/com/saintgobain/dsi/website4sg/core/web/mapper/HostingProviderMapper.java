package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.HostingProvider;

public class HostingProviderMapper {

    public static HostingProvider toHostingProvider(HostingProviderEntity hostingProviderEntity) {

        if (hostingProviderEntity == null) {
            return null;
        }

        return HostingProvider.builder()
                .code(hostingProviderEntity.getCode())
                .name(hostingProviderEntity.getName())
                .build();
    }

    public static Page<HostingProvider> toHostingProviderList(Page<HostingProviderEntity> hostingProviderEntities) {
        List<HostingProvider> hostingProviderList = hostingProviderEntities.getContent()
                .stream()
                .map(hostingProviderEntity -> toHostingProvider(hostingProviderEntity))
                .collect(Collectors.toList());

        return new PageImpl<HostingProvider>(hostingProviderList, hostingProviderEntities.getPageable(),
                hostingProviderEntities.getTotalElements());

    }

}
