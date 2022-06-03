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

import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.HostingProviderRepository;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.HostingProviderSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.HostingProvider;
import com.saintgobain.dsi.website4sg.core.web.mapper.HostingProviderMapper;

@Service
@Transactional
public class HostingProviderService {
    private final HostingProviderRepository hostingProviderRepository;

    public HostingProviderService(HostingProviderRepository hostingProviderRepository) {
        this.hostingProviderRepository = hostingProviderRepository;
    }

    @Transactional(readOnly = true)
    public Page<HostingProvider> getAllHostingProvider(Pageable pageable, String name, String search)
            throws Website4sgCoreException {

        Specification<HostingProviderEntity> spec = generateSpecification(name, search);

        Page<HostingProviderEntity> HostingProviderEntities = hostingProviderRepository.findAll(spec, pageable);

        return HostingProviderMapper.toHostingProviderList(HostingProviderEntities);
    }

    @Transactional
    public ResponseEntity<HostingProvider> createOrUpdateHostingProvider(String code, HostingProvider hostingProvider)
            throws Website4sgCoreException,
            URISyntaxException, UnsupportedEncodingException {

        HostingProviderEntity hostingProviderEntity = hostingProviderRepository.findByCode(code).orElseGet(
                () -> HostingProviderEntity.builder()
                        .code(code)
                        .build());

        boolean isNew = hostingProviderEntity.getHostingProviderId() == null;
        hostingProviderEntity.setName(hostingProvider.getName());

        if (isNew) {
            return ResponseEntity.created(new URI("/api/v1/hosting/providers/" + URLEncoder.encode(code,
                    StandardCharsets.UTF_8.name()))).body(
                            HostingProviderMapper.toHostingProvider(hostingProviderRepository.save(
                                    hostingProviderEntity)));
        } else {
            return ResponseEntity.ok(HostingProviderMapper.toHostingProvider(hostingProviderRepository.save(
                    hostingProviderEntity)));
        }
    }

    private Specification<HostingProviderEntity> generateSpecification(String name, String search) {
        Specification<HostingProviderEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(HostingProviderSpecification.nameSpecification(name));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;
    }

}
