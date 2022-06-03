package com.saintgobain.dsi.website4sg.core.web.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.website4sg.core.domain.referential.CmsEntity;
import com.saintgobain.dsi.website4sg.core.exception.Website4sgCoreException;
import com.saintgobain.dsi.website4sg.core.service.CmsService;
import com.saintgobain.dsi.website4sg.core.web.bean.Cms;
import com.saintgobain.dsi.website4sg.core.web.bean.CmsBody;
import com.saintgobain.dsi.website4sg.core.web.bean.CmsHeader;
import com.saintgobain.dsi.website4sg.core.web.mapper.CmsMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * The Class CmsEndpoint.
 */
@Api(value = "/v1/cms")
@RequestMapping("/v1/cms")
@RestController
public class CmsEndpoint {

    /** The cms service. */
    private CmsService cmsService;

    /**
     * Instantiates a new cms endpoint.
     *
     * @param cmsService the cms service
     */
    public CmsEndpoint(CmsService cmsService) {
        this.cmsService = cmsService;
    }

    /**
     * Gets the all cms.
     *
     * @param pageable the pageable
     * @param name the name
     * @param showEnable the show enable
     * @return the all cms
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping
    @ApiOperation(value = "Get all CMS managed by Website4sg", notes = "By default, fetchLimit parameter is set to 20", response = CmsHeader.class, responseContainer = "Page", authorizations = {
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
    public ResponseEntity<Page<CmsBody>> getAllCms(
            Pageable pageable,
            @ApiParam(value = "The name of environment") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "Show enable CMS") @RequestParam(value = "showEnable", required = false) Boolean showEnable,
            @ApiParam(value = "The search engine") @RequestParam(value = "search", required = false) String search)
            throws Website4sgCoreException {

        return ResponseEntity.ok(cmsService.getAllCmsBody(name, pageable, showEnable, search));
    }

    /**
     * Get the detail of the CMS given in parameter.
     *
     * @param cmsCode the functional Id of the parameter
     * @return the CMS detail
     * @throws Website4sgCoreException the website 4 sg core exception
     */
    @GetMapping("/{cmsCode}")
    @ApiOperation(value = "Get the CMS detail", notes = "", response = CmsBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<CmsBody> getCmsDetail(
            @ApiParam(value = "CMS Id", required = true) @PathVariable("cmsCode") String cmsCode)
            throws Website4sgCoreException {

        return ResponseEntity.ok(cmsService.getCmsBody(cmsCode));

    }

    /**
     * Create or update a CMS.
     *
     * @param cmsCode the functionnal Id of the CMS
     * @param cmsValue the CMS detail
     * @return the status of the requested operation
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws UnsupportedEncodingException
     */

    @PutMapping("/{cmsCode}")
    @ApiOperation(value = "Create Or Update the CMS", notes = "", response = CmsBody.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated"),
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "The body is empty"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<CmsBody> createOrUpdateCms(
            @ApiParam(value = "CMS Id", required = true) @PathVariable("cmsCode") String cmsCode,
            @ApiParam(value = "Cms detail", required = true) @Valid @RequestBody Cms cmsValue)
            throws URISyntaxException, UnsupportedEncodingException {

        Optional<CmsEntity> cmsExist = cmsService.selectByCode(cmsCode);
        if (cmsExist.isPresent()) {
            CmsEntity cmsToUpdate = cmsExist.get();
            cmsToUpdate.setName(cmsValue.getName());
            cmsToUpdate.setCodeRepositoryUrl(cmsValue.getCodeRepositoryUrl());
            cmsToUpdate.setBinaryRepositoryUrl(cmsValue.getBinaryRepositoryUrl());
            cmsToUpdate.setEnable(cmsValue.getEnable() == null ? true : cmsValue.getEnable());
            CmsEntity result = cmsService.save(cmsToUpdate);
            return ResponseEntity.ok(CmsMapper.toCmsBody(result));
        } else {
            CmsEntity newCms = CmsMapper.toCmsEntity(cmsValue, cmsCode);
            CmsEntity result = cmsService.save(newCms);
            return ResponseEntity.created(new URI("/api/v1/cms/" + URLEncoder.encode(result.getCode(),
                    StandardCharsets.UTF_8.name()))).body(CmsMapper.toCmsBody(
                            result));
        }
    }

}
