package com.saintgobain.dsi.website4sg.core.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DeploymentService;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/rundeck")
@RequestMapping("/v1/rundeck")
@RestController
public class RundeckEndpoint {

    private DeploymentService deploymentService;

    public RundeckEndpoint(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping("/callback")
    @ApiOperation(value = "Callback from Rundeck", response = Deployment.class, hidden = true, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Deployment> callbackRundeck(
            @ApiParam(value = "Rundeck Job Id", required = true) @RequestParam("id") String id,
            @ApiParam(value = "Deployment Status", required = true) @RequestParam("status") String status,
            @ApiParam(value = "Trigger", required = false) @RequestParam(value = "trigger", required = false) String trigger)
            throws Website4sgCoreException {

        return ResponseEntity.ok(deploymentService.callbackRundeck(id, status));
    }

}
