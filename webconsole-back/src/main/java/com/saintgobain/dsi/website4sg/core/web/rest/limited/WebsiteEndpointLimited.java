package com.saintgobain.dsi.website4sg.core.web.rest.limited;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
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
import com.saintgobain.dsi.website4sg.core.service.WebsiteService;
import com.saintgobain.dsi.website4sg.core.web.bean.DocrootHeader;
import com.saintgobain.dsi.website4sg.core.web.bean.Domain;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteDetailBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class WebsiteEndpointLimited.
 */
@Api(value = "/v1/websites")
@RequestMapping("/v1/websites")
@RestController
public class WebsiteEndpointLimited {

    /** The website service. */
    private WebsiteService websiteService;

    /**
     * Instantiates a new website endpoint limited.
     *
     * @param websiteService the website service
     */
    public WebsiteEndpointLimited(WebsiteService websiteService) {
        this.websiteService = websiteService;

    }

    /**
     * Gets the all websites.
     *
     * @param pageable the pageable
     * @param name the name
     * @param authentication the authentication
     * @return the all websites
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all websites", response = WebsiteDetailBody.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<WebsiteDetailBody>> getAllWebsites(
            Pageable pageable,
            Authentication authentication,
            @ApiParam(value = "The name of website") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "The qualys enabled flag") @RequestParam(value = "qualysEnabled", required = false) Boolean qualysEnabled,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search,
            @ApiParam(value = "Show enable Website") @RequestParam(value = "showEnable", required = false) Boolean showEnable)
            throws Website4sgCoreException {
        return ResponseEntity.ok(websiteService.getAllWebsites(pageable, name, qualysEnabled, authentication, search,
                showEnable));
    }

    /**
     * Gets the website.
     *
     * @param websiteCode the website code
     * @param authentication the authentication
     * @return the website
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{websiteCode}")
    @ApiOperation(value = "Get website by code", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<WebsiteDetailBody> getWebsite(
            @ApiParam(value = "Website code", required = true) @PathVariable("websiteCode") String websiteCode,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(websiteService.getWebsite(websiteCode, authentication));
    }

    /**
     * Gets the website docroots.
     *
     * @param websiteCode the website code
     * @param authentication the authentication
     * @return the website docroots
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{websiteCode}/docroots")
    @ApiOperation(value = "Get website docroots", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<List<? extends DocrootHeader>> getWebsiteDocroots(
            @ApiParam(value = "Website code", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "The environments flag for getting deployments") @RequestParam(value = "environments", required = false) Boolean environments,
            Authentication authentication) throws Website4sgCoreException {

        if (BooleanUtils.isTrue(environments)) {
            return ResponseEntity.ok(websiteService.getWebsiteDocrootenvironments(websiteCode,
                    authentication));
        }

        return ResponseEntity.ok(websiteService.getWebsiteDocroots(websiteCode,
                authentication));
    }

    /**
     * Gets the website domains.
     *
     * @param websiteCode the website code
     * @param waf the waf
     * @param authentication the authentication
     * @return the website domains
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{websiteCode}/domains")
    @ApiOperation(value = "Get website domains", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<List<Domain>> getWebsiteDomains(
            @ApiParam(value = "Website code", required = true) @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "The waf flag") @RequestParam(value = "waf", required = false) Boolean waf,
            @ApiParam(value = "The tree flag") @RequestParam(value = "tree", required = false) Boolean tree,
            @ApiParam(value = "Docroot Id") @RequestParam(value = "docrootCode", required = false) String docrootCode,
            @ApiParam(value = "Environment Id") @RequestParam(value = "environmentCode", required = false) String environmentCode,
            Authentication authentication) throws Website4sgCoreException {
        return ResponseEntity.ok(websiteService.getWebsiteDomains(websiteCode, waf, tree, docrootCode, environmentCode,
                authentication));
    }



}
