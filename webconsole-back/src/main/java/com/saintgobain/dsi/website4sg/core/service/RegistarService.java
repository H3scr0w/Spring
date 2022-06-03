package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.referential.RegistarEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.RegistarRepository;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.RegistarSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.Registar;
import com.saintgobain.dsi.website4sg.core.web.mapper.RegistarMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistarService {

    private final RegistarRepository registarRepository;

    @Transactional(readOnly = true)
    public Page<Registar> getAllRegistar(Pageable pageable, String name, String search) throws Website4sgCoreException {

        Specification<RegistarEntity> spec = generateSpecification(name, search);

        Page<RegistarEntity> registarEntities = registarRepository.findAll(spec, pageable);

        return RegistarMapper.toRegistarList(registarEntities);
    }

    @Transactional
    public ResponseEntity<Registar> createOrUpdateRegistar(String code, Registar registar)
            throws Website4sgCoreException,
            URISyntaxException, UnsupportedEncodingException {

        RegistarEntity registarEntity = registarRepository.findByCode(code).orElseGet(() -> RegistarEntity.builder()
                .code(code)
                .build());

        boolean isNew = registarEntity.getRegistarId() == null;
        registarEntity.setName(registar.getName());

        if (isNew) {
            return ResponseEntity.created(new URI("/api/v1/hosting/registars/" + URLEncoder.encode(code,
                    StandardCharsets.UTF_8.name()))).body(
                            RegistarMapper.toRegistar(registarRepository.save(registarEntity)));
        } else {
            return ResponseEntity.ok(RegistarMapper.toRegistar(registarRepository.save(registarEntity)));
        }
    }

    private Specification<RegistarEntity> generateSpecification(String name, String search) {
        Specification<RegistarEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(RegistarSpecification.nameSpecification(name));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;

    }

}
