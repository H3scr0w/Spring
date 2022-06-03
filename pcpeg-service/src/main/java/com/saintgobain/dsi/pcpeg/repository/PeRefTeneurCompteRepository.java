package com.saintgobain.dsi.pcpeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;

public interface PeRefTeneurCompteRepository extends JpaRepository<PeRefTeneurCompte, Short>,
        JpaSpecificationExecutor<PeRefTeneurCompte> {

}
