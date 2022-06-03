package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DeploymentService;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;


/**
 * The Class DeploymentEndpointLimited.
 */
@Api(value = "/v1/deployment")
@RequestMapping("/v1/deployment")
@RestController
public class DeploymentEndpointLimited {

    /** The deployment service. */
    private DeploymentService deploymentService;

    /**
     * Instantiates a new deployment endpoint limited.
     *
     * @param deploymentService the deployment service
     */
    public DeploymentEndpointLimited(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;

    }

    /**
     * Gets the alldeployments.
     *
     * @param pageable the pageable
     * @param authentication the authentication
     * @return the alldeployments
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all Deployment", response = Deployment.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    public ResponseEntity<Page<Deployment>> getAlldeployments(
            Pageable pageable,
            @ApiParam(value = "Deployment status", allowMultiple = true, type = "array", collectionFormat = "multi", allowableValues = "REQUESTED,IN_PROGRESS,ACCEPTED,SUCCEEDED,FAILED,ABORTED", example = "status=REQUESTED,IN_PROGRESS or status=REQUESTED&status=IN_PROGRESS", required = false) @RequestParam(value = "status", required = false) List<String> status,
            @ApiParam(value = "Search filter", required = false) @RequestParam(value = "search", required = false) String search,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(deploymentService.getAlldeployments(pageable, authentication, status, search));
    }

    /**
     * Gets the deployment detail.
     *
     * @param deploymentId the deployment id
     * @param authentication the authentication
     * @return the deployment detail
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{deploymentId}/")
    @ApiOperation(value = "Get the detail of a deployment", response = Deployment.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Deployment> getDeploymentDetail(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.getDeploymentDetail(deploymentId, authentication));
    }

    /**
     * Deploy.
     *
     * @param deploymentId the deployment id
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PostMapping("/{deploymentId}/deploy")
    @ApiOperation(value = "Validate a request deployment", response = Deployment.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Deployment> deploy(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.deploy(deploymentId, authentication));

    }

    /**
     * Gets the logs.
     *
     * @param deploymentId the deployment id
     * @param authentication the authentication
     * @return the logs
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{deploymentId}/logs")
    @ApiOperation(value = "Get logs from Rundeck", response = String.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<String> getLogs(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(deploymentService.getLogs(deploymentId, authentication));
    }

    /**
     * Cancel.
     *
     * @param deploymentId the deployment id
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PostMapping("/{deploymentId}/cancel")
    @ApiOperation(value = "Cancel a request deployment", response = Deployment.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Deployment> cancel(
            @ApiParam(value = "Deployment Id", required = true) @PathVariable("deploymentId") Long deploymentId,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.cancelDeploy(deploymentId, authentication));
    }

}
