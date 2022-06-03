package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.LoadBalancerEntity;

public interface LoadBalancerRepository extends JpaRepository<LoadBalancerEntity, Long>,
        JpaSpecificationExecutor<LoadBalancerEntity> {

    Optional<LoadBalancerEntity> findByCode(String code);

}
