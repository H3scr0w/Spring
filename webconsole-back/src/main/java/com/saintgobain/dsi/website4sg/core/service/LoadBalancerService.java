package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.referential.HostingProviderEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.LoadBalancerEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.HostingProviderRepository;
import com.saintgobain.dsi.website4sg.core.repository.LoadBalancerRepository;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.LoadBalancerSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.LoadBalancer;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.LoadBalancerMapper;

@Service
@Transactional
public class LoadBalancerService {

    private final LoadBalancerRepository loadBalancerRepository;

    private final HostingProviderRepository hostingProviderRepository;

    public LoadBalancerService(LoadBalancerRepository loadBalancerRepository,
            HostingProviderRepository hostingProviderRepository) {
        this.loadBalancerRepository = loadBalancerRepository;
        this.hostingProviderRepository = hostingProviderRepository;
    }

    @Transactional(readOnly = true)
    public Page<LoadBalancer> getAllLoadBalancer(Pageable pageable, String name, String search)
            throws Website4sgCoreException {

        Specification<LoadBalancerEntity> spec = generateSpecification(name, search, null);

        Page<LoadBalancerEntity> loadBalancerEntities = loadBalancerRepository.findAll(spec, pageable);

        return LoadBalancerMapper.toLoadBalancerList(loadBalancerEntities);
    }

    @Transactional
    public ResponseEntity<LoadBalancer> createOrUpdateLoadBalancer(String code, LoadBalancer loadBalancer)
            throws Website4sgCoreException,
            URISyntaxException, UnsupportedEncodingException {

        HostingProviderEntity hostingProviderEntity = hostingProviderRepository.findByCode(loadBalancer
                .getHostingProviderCode()).orElseThrow(
                        () -> new EntityNotFoundException(
                                ErrorCodes.HOSTING_PROVIDER_NOT_FOUND.name()));

        LoadBalancerEntity loadBalancerEntity = loadBalancerRepository.findByCode(code).orElseGet(
                () -> LoadBalancerMapper.toLoadBalancerEntity(loadBalancer).toBuilder().code(code)
                        .build());

        boolean isNew = loadBalancerEntity.getLoadBalancerId() == null;
        loadBalancerEntity.setHostingprovider(hostingProviderEntity);

        if (isNew) {

            return ResponseEntity.created(new URI("/api/v1/hosting/loadbalancers/" + URLEncoder.encode(code,
                    StandardCharsets.UTF_8.name()))).body(
                            LoadBalancerMapper.toLoadBalancer(loadBalancerRepository.save(loadBalancerEntity)));
        } else {

            loadBalancerEntity = LoadBalancerMapper.toNewLoadBalancerEntity(loadBalancerEntity, loadBalancer);

            return ResponseEntity.ok(LoadBalancerMapper.toLoadBalancer(loadBalancerRepository.save(
                    loadBalancerEntity)));
        }
    }

    public Specification<LoadBalancerEntity> generateSpecification(String name, String search, Long docrootEnvId) {
        Specification<LoadBalancerEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(LoadBalancerSpecification.nameSpecification(name));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        if (docrootEnvId != null) {
            spec = spec.and(LoadBalancerSpecification.joinWithDocrootEnv(docrootEnvId));
        }

        return spec;
    }

}
