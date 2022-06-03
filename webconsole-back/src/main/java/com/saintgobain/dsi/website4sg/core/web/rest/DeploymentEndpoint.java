package com.saintgobain.dsi.website4sg.core.web.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DeploymentCommandService;
import com.saintgobain.dsi.website4sg.core.service.DeploymentService;
import com.saintgobain.dsi.website4sg.core.web.bean.Command;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;
import com.saintgobain.dsi.website4sg.core.web.bean.DeploymentStatus;
import com.saintgobain.dsi.website4sg.core.web.bean.ProjectDeployment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class DeploymentEndpoint.
 */
@Api(value = "/v1/deployment")
@RequestMapping("/v1/deployment")
@RestController
public class DeploymentEndpoint {

    /** The deployment service. */
    private DeploymentService deploymentService;

    /** The deployment command service. */
    private DeploymentCommandService deploymentCommandService;

    /**
     * Instantiates a new deployment endpoint.
     *
     * @param deploymentService the deployment service
     * @param deploymentCommandService the deployment command service
     */
    public DeploymentEndpoint(DeploymentService deploymentService, DeploymentCommandService deploymentCommandService) {
        this.deploymentService = deploymentService;
        this.deploymentCommandService = deploymentCommandService;

    }

    /**
     * Update status.
     *
     * @param deploymentId the deployment id
     * @param status the status
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{deploymentId}/status")
    @ApiOperation(value = "Update the deployment status of specified deployment", response = Deployment.class, hidden = true, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Deployment> updateStatus(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            @ApiParam(value = "Status value", required = true) @Valid @RequestBody DeploymentStatus status)
            throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.updateStatus(deploymentId, status));

    }

    /**
     * Update rundeck job id.
     *
     * @param deploymentId the deployment id
     * @param deployment the deployment
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{deploymentId}/rundeckJobId")
    @ApiOperation(value = "Update the rundeck execution id", response = Deployment.class, hidden = true, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Deployment> updateRundeckJobId(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            @ApiParam(value = "Rundeck execution Id", required = true) @Valid @RequestBody Deployment deployment,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.updateRundeckJobId(deploymentId, deployment.getRundeckJobId(),
                authentication));

    }

    /**
     * Gets the configuration.
     *
     * @param deploymentId the deployment id
     * @param authentication the authentication
     * @return the configuration
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{deploymentId}/config")
    @ApiOperation(value = "Get the deployment configuration detail", notes = "Used by rundeck to retreive deployment information", hidden = true, response = ProjectDeployment.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<? extends ProjectDeployment> getConfiguration(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(deploymentService.getDeployConf(deploymentId, authentication));
    }

    /**
     * Gets the commands.
     *
     * @param deploymentId the deployment id
     * @param pageable the pageable
     * @param authentication the authentication
     * @return the commands
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{deploymentId}/commands")
    @ApiOperation(value = "List all commands to execute on server side before and after the deployment", response = Command.class, responseContainer = "List", authorizations = {
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
    public ResponseEntity<List<Command>> getCommands(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Pageable pageable,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentCommandService.getCommandsByDeploymentId(pageable,
                deploymentId, authentication));
    }

    /**
     * Creates the or update commands.
     *
     * @param deploymentId the deployment id
     * @param comands the comands
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping(path = "/{deploymentId}/commands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create or update commands", response = Command.class, responseContainer = "List", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deployment commands list created or updated"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<List<Command>> createOrUpdateCommands(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            @ApiParam(value = "List of commands to execute", required = true) @Valid @RequestBody List<Command> comands,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentCommandService.createOrUpdateCommandsByDeploymentId(deploymentId,
                comands,
                authentication));

    }

}
