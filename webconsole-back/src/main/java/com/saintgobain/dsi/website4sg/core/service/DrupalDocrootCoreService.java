package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.referential.DocrootEntity;
import com.saintgobain.dsi.website4sg.core.domain.referential.DrupalDocrootCoreEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.DocrootRepository;
import com.saintgobain.dsi.website4sg.core.repository.DrupalDocrootCoreRepository;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.specification.DocrootSpecification;
import com.saintgobain.dsi.website4sg.core.specification.DrupalDocrootCoreSpecification;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCore;
import com.saintgobain.dsi.website4sg.core.web.bean.DrupalDocrootCoreBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.DocrootMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.DrupalDocrootCoreMapper;

/**
 * The Class DrupalDocrootCoreService.
 */
@Service
@Transactional
public class DrupalDocrootCoreService {

    /** The drupaldocrootcore repository */
    private DrupalDocrootCoreRepository drupalDocrootCoreRepository;

    /** The docroot repository */
    private DocrootRepository docrootRepository;

    /** The project repository. */
    private ProjectRepository projectRepository;

    /** The project access control. */
    private ProjectAccessControl projectAccessControl;

    /** The admin. */
    private Admin admin;

    /**
     * Instantiates a new drupal docroot core service.
     *
     * @param drupalDocrootCoreRepository the drupal docroot core repository
     * @param docrootRepository the docroot repository
     * @param projectRepository the project repository
     * @param projectAccessControl the project access control
     * @param admin the admin
     */
    public DrupalDocrootCoreService(DrupalDocrootCoreRepository drupalDocrootCoreRepository,
            DocrootRepository docrootRepository, ProjectRepository projectRepository,
            ProjectAccessControl projectAccessControl, Admin admin) {
        this.drupalDocrootCoreRepository = drupalDocrootCoreRepository;
        this.docrootRepository = docrootRepository;
        this.projectRepository = projectRepository;
        this.projectAccessControl = projectAccessControl;
        this.admin = admin;

    }

    /**
     * Select by code.
     *
     * @param code the code
     * @return the optional
     */
    @Transactional(readOnly = true)
    public Optional<DrupalDocrootCoreEntity> selectByCode(String code) {
        return drupalDocrootCoreRepository.findByCode(code);
    }

    /**
     * Gets the all drupal docroot core header.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all drupal docroot core header
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<DrupalDocrootCoreBody> getAllDrupalDocrootCoreBody(Pageable pageable,
            String name,
            Authentication authentication, String search) throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        Specification<DrupalDocrootCoreEntity> spec = Specification.where(null);
        Page<DrupalDocrootCoreEntity> ddcList = new PageImpl<DrupalDocrootCoreEntity>(Arrays.asList(), pageable, 0);

        if (isAdmin) {
            spec = generateSpecification(name, null, search);

        } else {
            spec = generateSpecification(name, authentication.getName(), search);
        }

        ddcList = drupalDocrootCoreRepository.findAll(spec, pageable);
        return DrupalDocrootCoreMapper.toDocrootCoreBodyPage(ddcList, isAdmin);

    }

    /**
     * Gets the drupal docroot core body.
     *
     * @param drupaldocrootcoreCode the drupaldocrootcore code
     * @param authentication the authentication
     * @return the drupal docroot core body
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public DrupalDocrootCoreBody getDrupalDocrootCoreBody(String drupaldocrootcoreCode, Authentication authentication)
            throws Website4sgCoreException {

        DrupalDocrootCoreEntity ddc = drupalDocrootCoreRepository.findByCode(drupaldocrootcoreCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DRUPAL_PROJECT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(ddc.getProject(), authentication);
        }

        if (isAdmin || isAllowed) {
            return DrupalDocrootCoreMapper.toDrupalDocrootCoreBody(ddc, isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    /**
     * Gets the all docroots by drupal code.
     *
     * @param pageable the pageable
     * @param drupaldocrootCoreCode the drupaldocroot core code
     * @param authentication the authentication
     * @return the all docroots by drupal code
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public List<DocrootHeader> getAllDocrootsByDrupalCode(Pageable pageable,
            String drupaldocrootCoreCode,
            Authentication authentication) throws Website4sgCoreException {

        DrupalDocrootCoreEntity ddc = drupalDocrootCoreRepository.findByCode(drupaldocrootCoreCode).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.DRUPAL_PROJECT_NOT_FOUND.name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(ddc.getProject(), authentication);
        }

        if (isAdmin || isAllowed) {
            List<DocrootEntity> docroots = docrootRepository.findAll(DocrootSpecification.joinWithDrupal(
                    drupaldocrootCoreCode));
            return DocrootMapper.toDocrootHeaderList(docroots, isAdmin);
        } else {
            throw new ForbiddenException();
        }

    }

    /**
     * Creates the or update.
     *
     * @param drupaldocrootCoreCode the drupaldocroot core code
     * @param drupalDocrootCore the drupal docroot core
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     * @throws URISyntaxException the URI syntax exception
     * @throws UnsupportedEncodingException
     */
    @Transactional
    public ResponseEntity<DrupalDocrootCoreEntity> createOrUpdate(String drupaldocrootCoreCode,
            DrupalDocrootCore drupalDocrootCore) throws Website4sgCoreException, URISyntaxException,
            UnsupportedEncodingException {

        Optional<DrupalDocrootCoreEntity> ddcFound = drupalDocrootCoreRepository.findByCode(drupaldocrootCoreCode);

        if (ddcFound.isPresent()) {
            DrupalDocrootCoreEntity ddc = ddcFound.get();
            ddc.setCodeRepositoryUrl(drupalDocrootCore.getCodeRepositoryUrl());
            ddc.setBinaryRepositoryUrl(drupalDocrootCore.getBinaryRepositoryUrl());
            ddc.setName(drupalDocrootCore.getName());
            return ResponseEntity.ok(drupalDocrootCoreRepository.save(ddc));
        } else {

            DrupalDocrootCoreEntity newDrupal = DrupalDocrootCoreMapper.toDrupalDocrootCoreEntity(drupalDocrootCore,
                    drupaldocrootCoreCode);
            newDrupal = drupalDocrootCoreRepository.save(newDrupal);
            projectRepository.save(ProjectEntity.builder().drupaldocrootcore(newDrupal).build());
            return ResponseEntity.created(new URI("/api/v1/drupaldocrootcore/" + URLEncoder.encode(newDrupal.getCode(),
                    StandardCharsets.UTF_8.name()))).body(newDrupal);
        }

    }

    private Specification<DrupalDocrootCoreEntity> generateSpecification(String name, String email, String search) {
        Specification<DrupalDocrootCoreEntity> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(DrupalDocrootCoreSpecification.nameSpecification(name));
        }

        if (StringUtils.isNotBlank(email)) {
            spec = spec.and(DrupalDocrootCoreSpecification.joinWithUser(email));
        }

        if (StringUtils.isNotBlank(search)) {
            spec = spec.and(EntitySpecification.searchTextInAllColumns(search));
        }

        return spec;

    }

}
