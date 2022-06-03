package com.saintgobain.dsi.website4sg.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.website4sg.core.domain.referential.ServerEntity;

public interface ServerRepository extends JpaRepository<ServerEntity, Long>,
        JpaSpecificationExecutor<ServerEntity> {

    Optional<ServerEntity> findByHostname(String hostname);

    Page<ServerEntity> findAllByHostnameIgnoreCaseStartingWith(String name, Pageable pageable);
    
    Page<ServerEntity> findAllByDocrootenvironmentByServer_DocrootEnvironmentId(Long docrootEnvironmentId,
            Pageable pageable);
    
    Page<ServerEntity> findAllByDocrootenvironmentByServer_DocrootEnvironmentIdAndSshServer(Long docrootEnvironmentId,
            Boolean sshServer, Pageable pageable);
    
}
