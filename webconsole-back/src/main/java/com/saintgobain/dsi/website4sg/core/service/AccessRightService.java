package com.saintgobain.dsi.website4sg.core.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.users.AccessRightEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.RolesEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.AccessRightsRepository;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.repository.RolesRepository;
import com.saintgobain.dsi.website4sg.core.repository.UserRepository;
import com.saintgobain.dsi.website4sg.core.specification.AccessRightSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.AccessRightBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.AccessRightMapper;

@Service
@Transactional
public class AccessRightService {

    private AccessRightsRepository accessRightsRepository;

    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    private RolesRepository rolesRepository;

    public AccessRightService(AccessRightsRepository accessRightsRepository, ProjectRepository projectRepository,
            UserRepository userRepository, RolesRepository rolesRepository) {
        this.accessRightsRepository = accessRightsRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    @Transactional(readOnly = true)
    public Page<AccessRightBody> getAllAccessRights(Pageable pageable, String email)
            throws Website4sgCoreException {
        Specification<AccessRightEntity> spec = generateSpecification(email);
        return accessRightsRepository.findAll(spec, pageable).map(new AccessRightMapper());
    }

    @Transactional(readOnly = true)
    public AccessRightBody getAccessRightBody(Long accessRightId) throws Website4sgCoreException {

        AccessRightEntity accessRightFound = accessRightsRepository.findById(accessRightId)

                .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.ACCESS_RIGHT_NOT_FOUND.name()));

        return AccessRightMapper.toAccessRightBody(accessRightFound);
    }

    @Transactional
    public ResponseEntity<AccessRightBody> createOrUpdateAccessRight(Long accessRightId, AccessRightBody accessRight)
            throws Website4sgCoreException, URISyntaxException {

        ProjectTypeId projectTypeId = ProjectTypeId.getEnum(accessRight.getProjectType());
        ProjectEntity projectFound;

        switch (projectTypeId) {
        case D_DOCROOTCORE:
            projectFound = projectRepository.findByDrupaldocrootcoreCode(accessRight.getProjectCode()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.PROJECT_NOT_FOUND.name()));
            break;
        case D_WEBSITE:
            projectFound = projectRepository.findByWebsiteCode(accessRight.getProjectCode()).orElseThrow(
                    () -> new EntityNotFoundException(ErrorCodes.PROJECT_NOT_FOUND.name()));
            break;
        default:
            throw new NotImplementedException();
        }

        RolesEntity roleFound = rolesRepository.findByLabel(accessRight.getRoleLabel()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ROLE_NOT_FOUND.name()));

        UsersEntity userFound = userRepository.findByEmail(accessRight.getUserMail()).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND.name()));

        ProjectEntity project = projectFound;
        RolesEntity role = roleFound;
        UsersEntity user = userFound;

        Optional<AccessRightEntity> accessRightCheck = accessRightsRepository.findByProjectIdAndRoleIdAndUserId(
                project.getProjectId(), role.getRoleId(), user.getUserId());

        // Avoid any doublons when attempting to create or update a right
        if (accessRightCheck.isPresent()) {
            throw new BadRequestException(ErrorCodes.ACCESS_RIGHT_ALREADY_EXISTS.name());
        }

        AccessRightEntity accessRightFound = accessRightsRepository.findById(accessRightId)
                .orElseGet(() -> new AccessRightEntity());

        accessRightFound.setProject(project);
        accessRightFound.setRoles(role);
        accessRightFound.setUsers(user);
        boolean isNew = accessRightFound.getAccessRightId() == null;
        accessRightFound = accessRightsRepository.save(accessRightFound);
        if (isNew) {
            return ResponseEntity.created(new URI("/api/v1/accessrights/" + accessRightFound.getAccessRightId())).body(
                    AccessRightMapper.toAccessRightBody(accessRightFound));
        } else {
            return ResponseEntity.ok(AccessRightMapper.toAccessRightBody(accessRightFound));
        }

    }

    @Transactional
    public void delete(Long accessRightId) throws Website4sgCoreException {
        AccessRightEntity accessRightFound = accessRightsRepository.findById(accessRightId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCodes.ACCESS_RIGHT_NOT_FOUND.name()));
        accessRightsRepository.delete(accessRightFound);

    }

    private Specification<AccessRightEntity> generateSpecification(String email) {
        Specification<AccessRightEntity> spec = Specification.where(null);
        if (StringUtils.isNotBlank(email)) {
            spec = spec.and(AccessRightSpecification.joinWithUser(email));
        }
        return spec;
    }

}
