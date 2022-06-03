package com.saintgobain.dsi.website4sg.core.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.repository.ProjectRepository;
import com.saintgobain.dsi.website4sg.core.repository.UserRepository;
import com.saintgobain.dsi.website4sg.core.security.Admin;
import com.saintgobain.dsi.website4sg.core.security.ProjectAccessControl;
import com.saintgobain.dsi.website4sg.core.specification.EntitySpecification;
import com.saintgobain.dsi.website4sg.core.specification.ProjectSpecification;
import com.saintgobain.dsi.website4sg.core.web.bean.LDAPCredential;
import com.saintgobain.dsi.website4sg.core.web.bean.LDAPUser;
import com.saintgobain.dsi.website4sg.core.web.bean.ProjectBody;
import com.saintgobain.dsi.website4sg.core.web.bean.UserBody;
import com.saintgobain.dsi.website4sg.core.web.errors.ErrorCodes;
import com.saintgobain.dsi.website4sg.core.web.mapper.ProjectMapper;
import com.saintgobain.dsi.website4sg.core.web.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * The Class UserService.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    /** The user repository. */
    private final UserRepository userRepository;

    /** The project repository. */
    private final ProjectRepository projectRepository;

    /** The project access control. */
    private final ProjectAccessControl projectAccessControl;

    /** The admin. */
    private final Admin admin;

    /** The open dj rest template. */
    private final RestTemplate openDjRestTemplate;

    /**
     * Gets the all users projects.
     *
     * @param pageable the pageable
     * @param email the email
     * @param authentication the authentication
     * @param search the search
     * @return the all users projects
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<ProjectBody> getAllUsersProjects(Pageable pageable, String email,
            Authentication authentication, String search) throws Website4sgCoreException {

        selectByMail(email).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND
                .name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);

        Page<ProjectEntity> projects;

        if (isAdmin) {
            if (StringUtils.isEmpty(search)) {
                projects = projectRepository.findAllDistinctByAccessrightByProject_Users_EmailEquals(
                        email, pageable);
            } else {

                Specification<ProjectEntity> specWebsite = ProjectSpecification
                        .websiteIsNotNull().and(ProjectSpecification
                                .searchTextInWebsiteAllColumns(
                                        search)).and(ProjectSpecification
                                                .joinWithUser(email));

                Specification<ProjectEntity> specDrupal = ProjectSpecification
                        .drupalIsNotNull().and(ProjectSpecification
                                .searchTextInDrupalAllColumns(
                                        search)).and(ProjectSpecification
                                                .joinWithUser(email));

                Page<ProjectEntity> websites = projectRepository.findAll((Specification.<ProjectEntity> where(
                        specWebsite))
                                .and(ProjectSpecification
                                        .joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);

                Page<ProjectEntity> drupals = projectRepository.findAll((Specification.<ProjectEntity> where(
                        specDrupal))
                                .and(ProjectSpecification
                                        .joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);

                List<ProjectEntity> projectsList = Stream.concat(websites.stream(), drupals.stream()).limit(pageable
                        .getPageSize()).collect(Collectors
                                .toList());
                projects = new PageImpl<>(projectsList, pageable, projectsList.size());
            }

        } else {
            if (StringUtils.isEmpty(search)) {
                projects = projectRepository.findAllDistinctByAccessrightByProject_Users_EmailEquals(
                        authentication.getName(), pageable);
            } else {

                Specification<ProjectEntity> specWebsite = ProjectSpecification
                        .websiteIsNotNull().and(ProjectSpecification
                                .searchTextInWebsiteAllColumns(
                                        search));

                Specification<ProjectEntity> specDrupal = ProjectSpecification
                        .drupalIsNotNull().and(ProjectSpecification
                                .searchTextInDrupalAllColumns(
                                        search));

                Page<ProjectEntity> websites = projectRepository.findAll((Specification.<ProjectEntity> where(
                        specWebsite))
                                .and(ProjectSpecification
                                        .joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);

                Page<ProjectEntity> drupals = projectRepository.findAll((Specification.<ProjectEntity> where(
                        specDrupal))
                                .and(ProjectSpecification
                                        .joinWithUser(authentication
                                                .getPrincipal().toString())), pageable);

                List<ProjectEntity> projectsList = Stream.concat(websites.stream(), drupals.stream()).limit(pageable
                        .getPageSize()).collect(Collectors
                                .toList());
                projects = new PageImpl<>(projectsList, pageable, projectsList.size());

            }

        }

        return ProjectMapper.toProjectBodyPage(projects, isAdmin);
    }

    /**
     * Creates the or update.
     *
     * @param email the email
     * @param userBody the user body
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     * @throws URISyntaxException the URI syntax exception
     * @throws UnsupportedEncodingException
     */
    @Transactional
    public ResponseEntity<UserBody> createOrUpdate(String email,
            UserBody userBody) throws Website4sgCoreException, URISyntaxException, UnsupportedEncodingException {
        userBody.setAccessrightByUsers(null);
        UsersEntity user = userRepository.findByEmail(email).orElseGet(() -> UserMapper.toUserEntity(userBody)
                .toBuilder().email(
                email).build());

        boolean isNew = user.getUserId() == null;

        if (isNew) {
            user.setIsActive(true);
            user = userRepository.save(user);

            LDAPUser ldapUserFind = getLdapUser(email);

            if (ldapUserFind == null) {

                LDAPUser ldapUser = UserMapper.toLdapUser(userBody);
                HttpHeaders headers = new HttpHeaders();
                headers.set("If-None-Match", "*");

                HttpEntity<LDAPUser> body = new HttpEntity<LDAPUser>(ldapUser, headers);
                ldapUser = openDjRestTemplate.exchange("/users/" + user.getEmail(), HttpMethod.PUT, body,
                        LDAPUser.class)
                        .getBody();
            }

            return ResponseEntity.created(new URI("/api/v1/users/" + URLEncoder.encode(user.getEmail(),
                    StandardCharsets.UTF_8.name()))).body(UserMapper.toUserBody(
                            user, true));

        } else {

            user = UserMapper.toNewUserEntity(user, userBody);

            UserBody userToSend = UserMapper.toUserBody(userRepository.save(user), true);
            userToSend.setAccessrightByUsers(null);

            LDAPUser ldapUser = UserMapper.toLdapUser(userToSend);
            HttpHeaders headers = new HttpHeaders();
            headers.set("If-Match", "*");
            HttpEntity<LDAPUser> body = new HttpEntity<LDAPUser>(ldapUser, headers);

            ldapUser = openDjRestTemplate.exchange("/users/" + user.getEmail(), HttpMethod.PUT, body, LDAPUser.class)
                    .getBody();
            return ResponseEntity.ok(userToSend);

        }

    }

    /**
     * Gets the all users.
     *
     * @param pageable the pageable
     * @param authentication the authentication
     * @param search the search
     * @return the all users
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public Page<UserBody> getAllUsers(Pageable pageable, Authentication authentication, String search)
            throws Website4sgCoreException {

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        if (isAdmin) {
            if (StringUtils.isEmpty(search)) {
                return UserMapper.toUserBodyPage(userRepository.findAll(pageable), isAdmin);
            } else {
                return UserMapper.toUserBodyPage(userRepository.findAll(Specification.where(EntitySpecification
                        .searchTextInAllColumns(
                                search)), pageable), isAdmin);
            }

        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * Gets the user.
     *
     * @param email the email
     * @param authentication the authentication
     * @return the user
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional(readOnly = true)
    public UserBody getUser(String email, Authentication authentication)
            throws Website4sgCoreException {

        UsersEntity user = selectByMail(email).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND
                .name()));

        boolean isAdmin = projectAccessControl.isAdmin(authentication, admin);
        boolean isAllowed = false;

        if (!isAdmin && StringUtils.equals(email, authentication.getName())) {
            isAllowed = true;
        }

        if (isAdmin || isAllowed) {
            return UserMapper.toUserBody(user, isAdmin);
        } else {
            throw new ForbiddenException();
        }
    }

    /**
     * Update password.
     *
     * @param email the email
     * @param ldapCredential the ldap credential
     * @param authentication the authentication
     * @return the LDAP credential
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @Transactional
    public LDAPCredential updatePassword(String email, LDAPCredential ldapCredential, Authentication authentication)
            throws UnsupportedEncodingException, Website4sgCoreException {

        userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(ErrorCodes.USER_NOT_FOUND
                .name()));

        if (!StringUtils.equals(email, authentication.getName())) {
            throw new ForbiddenException();
        }

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LDAPCredential> request = new HttpEntity<LDAPCredential>(ldapCredential, headers);
        return openDjRestTemplate.exchange("/" + email, HttpMethod.POST, request, LDAPCredential.class).getBody();
    }

    @Transactional
    public UserBody updateProfile(String email, UserBody userBody, Authentication authentication)
            throws Website4sgCoreException {

        UsersEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.USER_NOT_FOUND
                .name()));

        if (!StringUtils.equals(email, authentication.getName())) {
            throw new ForbiddenException();
        }

        user = UserMapper.toNewProfile(user, userBody);

        UserBody userToSend = UserMapper.toUserBody(userRepository.save(user), false);
        return userToSend;
    }

    /**
     * Select by mail.
     *
     * @param email the email
     * @return the optional
     */
    @Transactional(readOnly = true)
    public Optional<UsersEntity> selectByMail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void lockUnlock(String email) throws Website4sgCoreException {
        UsersEntity user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                ErrorCodes.USER_NOT_FOUND
                .name()));
        Boolean value = false;
        if (BooleanUtils.isFalse(user.getIsActive())) {
            value = true;
        }
        user.setIsActive(value);
        userRepository.save(user);
    }

    private LDAPUser getLdapUser(String email) {
        try {
            return openDjRestTemplate.exchange("/users/" + email, HttpMethod.GET, null,
                    LDAPUser.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.NOT_FOUND.value()) {
                throw e;
            }
        }
        return null;
    }

}
