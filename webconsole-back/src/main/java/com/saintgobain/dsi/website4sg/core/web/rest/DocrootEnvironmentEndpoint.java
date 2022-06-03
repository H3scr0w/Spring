package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.DocrootEnvironmentService;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootEnvironmentDetail;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.bean.LoadBalancer;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.ServerHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsitesDeployed;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsitesDeployedHeader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/v1/hosting/docroots")
@RequestMapping("/v1/hosting/docroots")
@RestController
public class DocrootEnvironmentEndpoint {

    private DocrootEnvironmentService docrootEnvironmentService;

    public DocrootEnvironmentEndpoint(DocrootEnvironmentService docrootEnvironmentService) {
        this.docrootEnvironmentService = docrootEnvironmentService;
    }

    @GetMapping("/{docrootCode}/env/{environmentCode}")
    @ApiOperation(value = "Get the environment detail, associated to the specified docroot", response = DocrootEnvironmentDetail.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<DocrootEnvironmentDetail> getEnvironmentDetail(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            Authentication authentication) throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.getDocrootEnvironment(docrootCode, environmentCode,
                authentication));

    }

    @PutMapping("/{docrootCode}/env/{environmentCode}")
    @ApiOperation(value = "Create Or Update an environment on the specified docroot", notes = "", response = DocrootEnvironmentDetail.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<DocrootEnvironmentDetail> createOrUpdateDocrootEnvironment(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "DocrootEnvironement detail", required = true) @Valid @RequestBody DocrootEnvironmentDetail env)
            throws Website4sgCoreException, URISyntaxException, UnsupportedEncodingException {

        return docrootEnvironmentService.createOrUpdate(docrootCode, environmentCode, env);
    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}")
    @ApiOperation(value = "Remove the environement from the docroot", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> deleteEnvironmentDetail(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode)
            throws Website4sgCoreException {

        docrootEnvironmentService.delete(docrootCode, environmentCode);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{docrootCode}/env/{environmentCode}/servers")
    @ApiOperation(value = "Get all servers associated to the specified environment / docroot ", notes = "By default, fetchLimit parameter is set to 25", response = ServerHeader.class, responseContainer = "List", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<ServerHeader>> getAllServers(
            Pageable pageable,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "The search engine. Available value : ssh => to get ssh servers only", required = false) @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.getAllServers(pageable, docrootCode,
                environmentCode, search));
    }

    @PutMapping("/{docrootCode}/env/{environmentCode}/servers/{hostname}")
    @ApiOperation(value = "Add the specified server to the specified docroot / environment", response = ServerDetailBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<ServerDetailBody> addServer(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Hostname of the server to add", required = true) @PathVariable("hostname") String hostname)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.addServer(docrootCode, environmentCode, hostname));

    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}/servers/{hostname}")
    @ApiOperation(value = "Remove the specified server to the specified docroot / environment", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> removeServer(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Hostname of the server to add", required = true) @PathVariable("hostname") String hostname)
            throws Website4sgCoreException {

        docrootEnvironmentService.deleteServer(docrootCode, environmentCode, hostname);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{docrootCode}/env/{environmentCode}/websites")
    @ApiOperation(value = "Get all websites deployed on the specified docroot / environment", response = WebsitesDeployedHeader.class, responseContainer = "List", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<WebsitesDeployedHeader>> getAllWebsites(
            Pageable pageable,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode)
            throws Website4sgCoreException {
        return ResponseEntity.ok(docrootEnvironmentService.getAllWebsitesDeployed(pageable, docrootCode,
                environmentCode));
    }

    @GetMapping("/{docrootCode}/env/{environmentCode}/websites/{websiteCode}")
    @ApiOperation(value = "Get the website detail", notes = "", response = WebsiteDetailBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebsiteDetailBody> getWebsiteDetail(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.getWebsiteDeployed(docrootCode,
                environmentCode, websiteCode));

    }

    @PutMapping("/{docrootCode}/env/{environmentCode}/websites/{websiteCode}")
    @ApiOperation(value = "Add or update a website on the specified docroot / environment", notes = "", response = WebsiteDetailBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<WebsiteDetailBody> addWebsite(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "The websiteDeployed detail", required = true) @Valid @RequestBody WebsitesDeployed websitesDeployed)
            throws Website4sgCoreException, URISyntaxException, UnsupportedEncodingException {

        return docrootEnvironmentService.addWebsite(docrootCode, environmentCode, websiteCode, websitesDeployed);
    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}/websites/{websiteCode}")
    @ApiOperation(value = "Remove website from the specified docroot / environment", notes = "", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> removeWebsite(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode)
            throws Website4sgCoreException {

        docrootEnvironmentService.deleteWebsite(docrootCode, environmentCode, websiteCode);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{docrootCode}/env/{environmentCode}/domains")
    @ApiOperation(value = "Get all domains on the specified docroot / environment", response = Domain.class, responseContainer = "Page", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<Domain>> getAllDomains(
            Pageable pageable,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode)
            throws Website4sgCoreException {
        return ResponseEntity.ok(docrootEnvironmentService.getAllDomains(pageable, docrootCode,
                environmentCode));
    }

    @PutMapping("/{docrootCode}/env/{environmentCode}/domains/{domainCode}")
    @ApiOperation(value = "Add a domain on the specified docroot / environment", notes = "", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Domain> addDomain(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.addDomain(docrootCode, environmentCode, domainCode));
    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}/domains/{domainCode}")
    @ApiOperation(value = "Remove the specified domain to the specified docroot / environment", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> removeDomain(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("domainCode") String domainCode)
            throws Website4sgCoreException {

        docrootEnvironmentService.deleteDomain(docrootCode, environmentCode, domainCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{docrootCode}/env/{environmentCode}/loadbalancers")
    @ApiOperation(value = "Get all loadbalancers on the specified docroot / environment", response = LoadBalancer.class, responseContainer = "Page", authorizations = {
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
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<LoadBalancer>> getAllLoadBalancers(
            Pageable pageable,
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "The search engine", required = false) @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {
        return ResponseEntity.ok(docrootEnvironmentService.getAllLoadBalancers(pageable, docrootCode,
                environmentCode, search));
    }

    @PutMapping("/{docrootCode}/env/{environmentCode}/loadbalancers/{loadBalancerCode}")
    @ApiOperation(value = "Add a loadbalancer on the specified docroot / environment", notes = "", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<LoadBalancer> addLoadBalancer(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "LoadBalancer Id", required = true) @PathVariable("loadBalancerCode") String loadBalancerCode)
            throws Website4sgCoreException {

        return ResponseEntity.ok(docrootEnvironmentService.addLoadBalancer(docrootCode, environmentCode,
                loadBalancerCode));
    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}/loadbalancers/{loadBalancerCode}")
    @ApiOperation(value = "Remove the specified loadbalancer to the specified docroot / environment", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Void> removeLoadBalancer(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Domain Id", required = true) @PathVariable("loadBalancerCode") String loadBalancerCode)
            throws Website4sgCoreException {

        docrootEnvironmentService.deleteLoadBalancer(docrootCode, environmentCode, loadBalancerCode);
        return ResponseEntity.ok().build();

    }
    
    @PutMapping("/{docrootCode}/env/{environmentCode}/websites/{websiteCode}/domains/{parentDomainCode}/redirections/{childDomainCode}")
    @ApiOperation(value = "Attach existing domain to another and become redirection", notes = "", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Domain> attachRedirection(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "Parent Domain Id", required = true) @PathVariable("parentDomainCode") String parentDomainCode,
            @ApiParam(value = "Child Domain Id", required = true) @PathVariable("childDomainCode") String childDomainCode)
            throws Website4sgCoreException {
        return ResponseEntity.ok(docrootEnvironmentService.attachRedirection(docrootCode, environmentCode, websiteCode,
                parentDomainCode,
                childDomainCode));

    }

    @DeleteMapping("/{docrootCode}/env/{environmentCode}/websites/{websiteCode}/domains/{parentDomainCode}/redirections/{childDomainCode}")
    @ApiOperation(value = "Detach existing redirection domain from another and become contribution", notes = "", response = Domain.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Domain> detachRedirection(
            @ApiParam(value = "Docroot Id", required = true) @PathVariable("docrootCode") String docrootCode,
            @ApiParam(value = "Environment Id", required = true) @PathVariable("environmentCode") String environmentCode,
            @ApiParam(value = "Website Id", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "Parent Domain Id", required = true) @PathVariable("parentDomainCode") String parentDomainCode,
            @ApiParam(value = "Child Domain Id", required = true) @PathVariable("childDomainCode") String childDomainCode)
            throws Website4sgCoreException {
        docrootEnvironmentService.detachRedirection(docrootCode, environmentCode, websiteCode,
                parentDomainCode,
                childDomainCode);
        return ResponseEntity.ok().build();

    }


}
