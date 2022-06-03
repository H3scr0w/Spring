package com.saintgobain.dsi.website4sg.core.service;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.repository.UserRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.UserSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.UserBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * The Class ProjectService.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    /** The project repository. */
    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    /**
     * Gets the all users.
     *
     * @param projectType the project type
     * @param projectCode the project code
     * @param pageable the pageable
     * @param authentication the authentication
     * @return the all users
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<UserBody> getAllUsers(String projectType, String projectCode, Pageable pageable,
            Authentication authentication, String search)
            throws Website4sgCoreException {

        if (!projectType.equals(ProjectTypeId.D_DOCROOTCORE.getName()) && !projectType.equals(ProjectTypeId.D_WEBSITE
                .getName())) {
            throw new NotImplementedException();
        }

        ProjectEntity projectEntity;
        if (projectType.equals(ProjectTypeId.D_WEBSITE.getName())) {
            projectEntity = projectRepository.findByWebsiteCode(projectCode).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.PROJECT_NOT_FOUND.name()));
        } else {
            projectEntity = projectRepository.findByDrupaldocrootcoreCode(projectCode).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.PROJECT_NOT_FOUND.name()));
        }

        boolean isAllowed = false;
        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        if (!isAdmin) {
            isAllowed = filterAccessToProject(projectEntity, authentication);
        }
        if (isAdmin || isAllowed) {

            Page<UsersEntity> users;

            if (StringUtils.isEmpty(search)) {
                users = userRepository.findAllDistinctByAccessrightByUsers_ProjectId(projectEntity
                        .getProjectId(),
                        pageable);
            } else {
                Specification<UsersEntity> spec = Specification.where(UserSpecification.joinWithProject(projectEntity
                        .getProjectId()).and(EntitySpecification.searchTextInAllColumns(search)));
                users = userRepository.findAll(spec, pageable);
            }

            return UserMapper.toUserBodyPage(users, isAdmin, projectType, projectCode);
        } else {
            throw new ForbiddenException();
        }
    }

    private boolean filterAccessToProject(ProjectEntity project, Authentication authentication) {
        return projectAccessControl.isAuthorized(project, authentication);
    }

}
