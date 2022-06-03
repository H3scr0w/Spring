package com.saintgobain.dsi.website4sg.core.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;

import lombok.RequiredArgsConstructor;

/**
 * The Class RepositoryService.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RepositoryService {

    private final RestTemplate repositoryRestTemplate;

    /** The website repository. */
    private final ProjectRepository projectRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    @Transactional(readOnly = true)
    public String getRepository(String repositoryId, Authentication authentication)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        ProjectEntity project = projectRepository.findByWebsiteCode(repositoryId).orElseGet(() -> {
            return projectRepository.findByDrupaldocrootcoreCode(repositoryId).orElseThrow(
                    () -> new EntityNotFoundException(
                            ErrorCodes.PROJECT_NOT_FOUND.name()));
        });

        if (!isAdmin) {
            isAllowed = projectAccessControl.isAuthorized(project, authentication);
        }

        if (isAdmin || isAllowed) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/version/run/");
            HttpEntity<String> body = new HttpEntity<String>(repositoryId);
            return repositoryRestTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, body,
                    String.class)
                    .getBody();
        } else {
            throw new ForbiddenException();
        }

    }

}
