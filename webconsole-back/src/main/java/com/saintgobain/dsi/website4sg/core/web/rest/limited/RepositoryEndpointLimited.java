package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.RepositoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class CmsEndpoint.
 */
@Api(value = "/v1/repository")
@RequestMapping("/v1/repository")
@RestController
public class RepositoryEndpointLimited {

    private final RepositoryService repositoryService;

    public RepositoryEndpointLimited(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{repositoryId}")
    @ApiOperation(value = "Get repository by repositoryId", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<String> getRepository(
            @ApiParam(value = "repository id", required = true) @PathVariable("repositoryId") String repositoryId,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(repositoryService.getRepository(repositoryId, authentication));
    }

}
