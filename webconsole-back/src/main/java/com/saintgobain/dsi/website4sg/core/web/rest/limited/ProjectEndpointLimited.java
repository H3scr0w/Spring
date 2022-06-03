package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.ProjectService;
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
 * The Class ProjectEndpointLimited.
 */
@Api(value = "/v1/projects")
@RequestMapping("/v1/projects")
@RestController
public class ProjectEndpointLimited {

    /** The project service. */
    private ProjectService projectService;

    /**
     * Instantiates a new project endpoint limited.
     *
     * @param projectService the project service
     */
    public ProjectEndpointLimited(ProjectService projectService) {
        this.projectService = projectService;

    }

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
    @GetMapping("/{projectType}/{projectCode}/users")
    @ApiOperation(value = "Get all users", authorizations = {
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
    public ResponseEntity<Page<UserBody>> getAllUsers(
            @ApiParam(value = "Project type", required = true) @PathVariable("projectType") String projectType,
            @ApiParam(value = "Project code", required = true) @PathVariable("projectCode") String projectCode,
            @ApiParam(value = "Search filter", required = false) @RequestParam(value = "search", required = false) String search,
            Pageable pageable,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(projectService.getAllUsers(projectType, projectCode, pageable,
                authentication, search));
    }

}
