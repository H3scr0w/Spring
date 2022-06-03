package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

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
 * The Class UserEndpoint.
 */
@Api(value = "/v1/users")
@RequestMapping("/v1/users")
@RestController
public class UserEndpoint {

    /** The user service. */
    private final UserService userService;

    /**
     * Instantiates a new user endpoint.
     *
     * @param userService the user service
     */
    public UserEndpoint(UserService userService) {
        this.userService = userService;
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
    @GetMapping
    @ApiOperation(value = "Get all users", response = UserBody.class, responseContainer = "Page", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<UserBody>> getAllUsers(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "Search filter", required = false) @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(userService.getAllUsers(pageable, authentication, search));
    }

    /**
     * Creates the or update user.
     *
     * @param email the email
     * @param userBody the user body
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     * @throws URISyntaxException the URI syntax exception
     */
    @PutMapping("/{email}")
    @ApiOperation(value = "Create Or Update the User", notes = "", response = UserBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<UserBody> createOrUpdateUser(
            @ApiParam(value = "Email", required = true) @PathVariable("email") String email,
            @ApiParam(value = "User Body", required = true) @Valid @RequestBody UserBody userBody)
            throws Website4sgCoreException, URISyntaxException, UnsupportedEncodingException {

        return userService.createOrUpdate(email, userBody);
    }

    @PutMapping("/lock-unlock/{email}")
    @ApiOperation(value = "Lock or Unlock the User", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The path is incorrect"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> lockUnlockUser(
            @ApiParam(value = "Email", required = true) @PathVariable("email") String email)
            throws Website4sgCoreException {
        userService.lockUnlock(email);
        return ResponseEntity.ok().build();
    }

}
