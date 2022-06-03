package com.saintgobain.dsi.website4sg.core.web.rest;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.WebsiteService;
import com.saintgobain.dsi.website4sg.core.web.bean.Website;
import com.saintgobain.dsi.website4sg.core.web.bean.WebsiteBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class WebsiteEndpoint.
 */
@Api(value = "/v1/websites")
@RequestMapping("/v1/websites")
@RestController
public class WebsiteEndpoint {

    /** The website service. */
    private final WebsiteService websiteService;

    /**
     * Instantiates a new website endpoint.
     *
     * @param websiteService the website service
     */
    public WebsiteEndpoint(WebsiteService websiteService) {
        this.websiteService = websiteService;

    }

    /**
     * Creates the or update website.
     *
     * @param websiteCode the website code
     * @param website the website
     * @param authentication the authentication
     * @return the response entity
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @PutMapping("/{websiteCode}")
    @ApiOperation(value = "Update or add a website")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    public ResponseEntity<WebsiteBody> createOrUpdateWebsite(
            @ApiParam(value = "WebsiteCode") @PathVariable("websiteCode") String websiteCode,
            @ApiParam(value = "Website object", required = true) @Valid @RequestBody Website website,
            Authentication authentication)
            throws Website4sgCoreException {

        return ResponseEntity.ok(websiteService.createOrUpdateWebsite(websiteCode, website, authentication));

    }

}
