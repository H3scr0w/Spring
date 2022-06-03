package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.UserService;
import com.saintgobain.dsi.website4sg.core.web.bean.LDAPCredential;
import com.saintgobain.dsi.website4sg.core.web.bean.ProjectBody;
import com.saintgobain.dsi.website4sg.core.web.bean.UserBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class UserEndpointLimited.
 */
@Api(value = "/v1/users")
@RequestMapping("/v1/users")
@RestController
public class UserEndpointLimited {

    /** The user service. */
    private final UserService userService;

    /**
     * Instantiates a new user endpoint.
     *
     * @param userService the user service
     */
    public UserEndpointLimited(UserService userService) {
        this.userService = userService;
    }


    /**
     * Gets the all users projects.
     *
     * @param pageable the pageable
     * @param email the email
     * @param search the search
     * @param authentication the authentication
     * @return the all users projects
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{email}/projects")
    @ApiOperation(value = "Get all users projects", response = ProjectBody.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Page<ProjectBody>> getAllUsersProjects(
            Pageable pageable,
            @ApiParam(value = "email") @PathVariable("email") String email,
            @ApiParam(value = "Search filter", required = false) @RequestParam(value = "search", required = false) String search,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(userService.getAllUsersProjects(pageable, email, authentication, search));
    }

    /**
     * Gets the user.
     *
     * @param email the email
     * @param authentication the authentication
     * @return the user
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{email}")
    @ApiOperation(value = "Get user", response = UserBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<UserBody> getUser(
            @ApiParam(value = "email") @PathVariable("email") String email,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(userService.getUser(email, authentication));
    }

    /**
     * Update password.
     *
     * @param email the email
     * @param ldapCredential the ldap credential
     * @param authentication the authentication
     * @return the response entity
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{email}/authentication/password")
    @ApiOperation(value = "Update password", notes = "", response = LDAPCredential.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Void> updatePassword(
            @ApiParam(value = "Email", required = true) @PathVariable("email") String email,
            @ApiParam(value = "User Body", required = true) @Valid @RequestBody LDAPCredential ldapCredential,
            Authentication authentication)
            throws UnsupportedEncodingException, Website4sgCoreException {

        userService.updatePassword(email, ldapCredential, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * Update profile.
     *
     * @param email the email
     * @param userBody the user body
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/profile/{email}")
    @ApiOperation(value = "Update the user profile", notes = "", response = UserBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<UserBody> updateProfile(
            @ApiParam(value = "Email", required = true) @PathVariable("email") String email,
            @ApiParam(value = "User Body", required = true) @Valid @RequestBody UserBody userBody,
            Authentication authentication)
            throws Website4sgCoreException {

        return ResponseEntity.ok(userService.updateProfile(email, userBody, authentication));
    }

}
