package com.saintgobain.dsi.website4sg.core.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.CmsRepository;
import com.saintgobain.dsi.website4sg.core.specification.CmsSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.CmsBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.CmsMapper;

@Service
@Transactional
public class CmsService {

    private CmsRepository cmsRepository;

    public CmsService(CmsRepository cmsRepository) {
        this.cmsRepository = cmsRepository;
    }

    /**
     * Gets the all cms body.
     *
     * @param name the name
     * @param pageable the pageable
     * @param showEnable the show enable
     * @return the all cms body
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<CmsBody> getAllCmsBody(String name, Pageable pageable, Boolean showEnable, String search)
            throws Website4sgCoreException {
        Specification<CmsEntity> spec = generateSpecification(name, showEnable, search);
        Page<CmsEntity> results = cmsRepository.findAll(spec, pageable);
        return CmsMapper.toCmsBodyPage(results);
    }

    @Transactional(readOnly = true)
    public CmsBody getCmsBody(String cmsCode) throws Website4sgCoreException {
        CmsEntity resultFind = selectByCode(cmsCode).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.CMS_NOT_FOUND.name()));
        return CmsMapper.toCmsBody(resultFind);
    }

    @Transactional(readOnly = true)
    public Optional<CmsEntity> selectByCode(String code) {
        return cmsRepository.findByCode(code);
    }

    public CmsEntity save(CmsEntity cmsEntity) {
        return cmsRepository.save(cmsEntity);
    }

    private Specification<CmsEntity> generateSpecification(String name, Boolean showEnable, String search) {
        Specification<CmsEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(CmsSpecification.name(name));
        }

        if (showEnable != null) {
            spec = spec.and(CmsSpecification.enable(showEnable));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

}
