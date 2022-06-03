package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DeploymentService;
import com.saintgobain.dsi.website4sg.core.web.bean.Deployment;
import com.saintgobain.dsi.website4sg.core.web.bean.Request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/request")
@RequestMapping("/v1/request")
@RestController
public class RequestEndpointLimited {

    private DeploymentService deploymentService;

    public RequestEndpointLimited(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    @ApiOperation(value = "Send a deployment request", response = Deployment.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<Deployment> sendDeploymentRequest(@Valid @RequestBody Request request,
            Authentication authentication)
            throws Website4sgCoreException, URISyntaxException {

        Deployment result = deploymentService.requestDeploy(request, authentication);
        result.setRundeckJobId(null);
        return ResponseEntity.created(new URI("/api/v1/deployment/" + result.getDeploymentId())).body(result);

    }

}
