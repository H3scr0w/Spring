package com.saintgobain.dsi.website4sg.core.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.website4sg.core.config.SaintGobainProperties;
import com.saintgobain.dsi.website4sg.core.domain.deployment.DeploymentEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.RundeckArgs;

@Service
public class RundeckService {

    private final RestTemplate rundeckRestTemplate;

    private SaintGobainProperties properties;

    public RundeckService(RestTemplate rundeckRestTemplate, SaintGobainProperties properties) {
        this.rundeckRestTemplate = rundeckRestTemplate;
        this.properties = properties;
    }

    public String getLogs(DeploymentEntity deploymentEntity) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/execution/"
                + deploymentEntity.getRundeckJobId() + "/output.json");

        return rundeckRestTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, null, String.class)
                .getBody();

    }

    public ResponseEntity<String> postExecution(String rundeckJobId, String wsipDeploymentId,
            String wsipEnvironmentCode) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/job/"
                + rundeckJobId + "/executions");

        /*
         * Build the param String Example :
         * {"argString":"-PARAM_URL 'https://<back_host>/v1/deployment/56' -ENVCODE dev " }
         */

        StringBuilder params = new StringBuilder();
        params.append("-PARAM_URL '");
        params.append(buildParamUrl(wsipDeploymentId));
        params.append("' -ENVCODE ");
        params.append(wsipEnvironmentCode);

        HttpEntity<RundeckArgs> request = new HttpEntity<>(RundeckArgs.builder().argString(params.toString()).build());

        ResponseEntity<String> result = null;

        try {
            result = rundeckRestTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, request,
                    String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.CONFLICT.value()) {
                throw e;
            }
        }
        return result;

    }

    /*
     * Generate the deploymentDetailUrl http://wildfly:8080/v1/deployment/56/
     */
    private String buildParamUrl(String wsipDeploymentId) {
        UriComponentsBuilder deploymentDetailUrl = UriComponentsBuilder.fromUriString(properties.getApi()
                .getDeployment().getUrl());
        deploymentDetailUrl.path("/" + wsipDeploymentId);
        return deploymentDetailUrl.build().toString();
    }

}
