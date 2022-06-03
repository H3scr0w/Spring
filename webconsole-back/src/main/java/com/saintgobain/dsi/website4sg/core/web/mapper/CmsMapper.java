package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.Cms;
import com.saintgobain.dsi.website4sg.core.web.bean.CmsBody;
import com.saintgobain.dsi.website4sg.core.web.bean.CmsHeader;

public class CmsMapper {

    public static Cms toCms(CmsEntity cmsEntity) {
        if (cmsEntity == null) {
            return null;
        }
        return Cms.builder().codeRepositoryUrl(cmsEntity.getCodeRepositoryUrl()).build();
    }

    public static CmsHeader toCmsHeader(CmsEntity cmsEntity) {
        if (cmsEntity == null) {
            return null;
        }
        return CmsHeader.builder().name(cmsEntity.getName()).build();
    }

    public static List<CmsHeader> toCmsHeaderList(List<CmsEntity> cmsEntities) {
        return cmsEntities.stream().map(cmsEntity -> toCmsHeader(cmsEntity)).collect(Collectors.toList());
    }

    public static List<CmsBody> toCmsBodyList(List<CmsEntity> cmsEntities) {
        return cmsEntities.stream().map(cms -> toCmsBody(cms)).collect(Collectors.toList());
    }

    public static Page<CmsBody> toCmsBodyPage(Page<CmsEntity> cmsEntities) {
        List<CmsBody> cmsBodies = cmsEntities.getContent().stream().map(cms -> toCmsBody(cms)).collect(Collectors
                .toList());
        return new PageImpl<CmsBody>(cmsBodies, cmsEntities.getPageable(), cmsEntities.getTotalElements());
    }

    public static CmsBody toCmsBody(CmsEntity cmsEntity) {
        if (cmsEntity == null) {
            return null;
        }
        return CmsBody.builder()
                .code(cmsEntity.getCode())
                .name(cmsEntity.getName())
                .codeRepositoryUrl(cmsEntity.getCodeRepositoryUrl())
                .binaryRepositoryUrl(cmsEntity.getBinaryRepositoryUrl())
                .build();
    }

    public static CmsEntity toCmsEntity(Cms cms, String code) {
        if (cms == null) {
            return null;
        }
        return CmsEntity.builder()
                .enable(cms.getEnable() == null ? true : cms.getEnable())
                .name(cms.getName())
                .code(code)
                .codeRepositoryUrl(cms.getCodeRepositoryUrl())
                .binaryRepositoryUrl(cms.getBinaryRepositoryUrl())
                .build();
    }

}
