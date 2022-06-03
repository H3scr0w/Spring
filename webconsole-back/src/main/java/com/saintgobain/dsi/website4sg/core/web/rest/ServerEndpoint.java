package com.saintgobain.dsi.website4sg.core.web.rest;

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
import com.saintgobain.dsi.website4sg.core.service.ServerService;
import com.saintgobain.dsi.website4sg.core.web.bean.Server;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerDetailBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class CmsEndpoint.
 */
@Api(value = "/v1/servers")
@RequestMapping("/v1/servers")
@RestController
public class ServerEndpoint {

    /** The server service. */
    private final ServerService serverService;

    /**
     * Instantiates a new server endpoint.
     *
     * @param serverService the server service
     */
    public ServerEndpoint(ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * Gets the all servers.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all servers
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all servers")
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
    public ResponseEntity<Page<ServerDetailBody>> getAllServers(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The name of environment") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(serverService.getAllServers(pageable, name, authentication, search));
    }

    /**
     * Gets the server.
     *
     * @param hostname the hostname
     * @param authentication the authentication
     * @return the server
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{hostname}")
    @ApiOperation(value = "Get server by hostname")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<ServerDetailBody> getServer(
            @ApiParam(value = "host name", required = true) @PathVariable("hostname") String hostname,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(serverService.getServer(hostname, authentication));
    }

    /**
     * Creates the or update server.
     *
     * @param hostname the hostname
     * @param server the server
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{hostname}")
    @ApiOperation(value = "Update or add a server")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<ServerDetailBody> createOrUpdateServer(
            @ApiParam(value = "host name", required = true) @PathVariable("hostname") String hostname,
            @ApiParam(value = "Server object", required = true) @Valid @RequestBody Server server,
            Authentication authentication)
            throws Website4sgCoreException {

        return ResponseEntity.ok(serverService.createOrUpdateServer(hostname, server, authentication));

    }
}
