package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.referential.LoadBalancerEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.LoadBalancer;

public class LoadBalancerMapper {

    public static LoadBalancer toLoadBalancer(LoadBalancerEntity loadBalancerEntity) {
        if (loadBalancerEntity == null) {
            return null;
        }

        return LoadBalancer.builder()
                .code(loadBalancerEntity.getCode())
                .name(loadBalancerEntity.getName())
                .fqdn(loadBalancerEntity.getFqdn())
                .ip(loadBalancerEntity.getIp())
                .ip2(loadBalancerEntity.getIp2())
                .hostingProviderCode(loadBalancerEntity.getHostingprovider().getCode())
                .hostingProviderName(loadBalancerEntity.getHostingprovider().getName())
                .build();
    }

    public static Page<LoadBalancer> toLoadBalancerList(Page<LoadBalancerEntity> loadBalancerEntities) {
        List<LoadBalancer> loadBalancerList = loadBalancerEntities.getContent()
                .stream()
                .map(loadBalancerEntity -> toLoadBalancer(loadBalancerEntity))
                .collect(Collectors.toList());

        return new PageImpl<LoadBalancer>(loadBalancerList, loadBalancerEntities.getPageable(), loadBalancerEntities
                .getTotalElements());

    }

    public static LoadBalancerEntity toLoadBalancerEntity(LoadBalancer loadBalancer) {
        if (loadBalancer == null) {
            return null;
        }

        return LoadBalancerEntity.builder()
                .name(loadBalancer.getName())
                .fqdn(loadBalancer.getFqdn())
                .ip(loadBalancer.getIp())
                .ip2(loadBalancer.getIp2())
                .build();
    }

    public static LoadBalancerEntity toNewLoadBalancerEntity(LoadBalancerEntity loadBalancerEntity,
            LoadBalancer loadBalancer) {
        if (loadBalancer == null) {
            return null;
        }

        return loadBalancerEntity.toBuilder()
                .name(loadBalancer.getName())
                .fqdn(loadBalancer.getFqdn())
                .ip(loadBalancer.getIp())
                .ip2(loadBalancer.getIp2())
                .build();
    }

}
