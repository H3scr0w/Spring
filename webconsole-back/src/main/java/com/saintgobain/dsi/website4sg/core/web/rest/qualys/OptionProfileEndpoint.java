package com.saintgobain.dsi.website4sg.core.web.rest.qualys;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.website4sg.core.service.qualys.OptionProfileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api(value = "/tools/qualys/profiles")
@RequestMapping("/tools/qualys/profiles")
@RestController
public class OptionProfileEndpoint {

    private final OptionProfileService optionProfileService;

    public OptionProfileEndpoint(OptionProfileService optionProfileService) {
        this.optionProfileService = optionProfileService;
    }

    @PostMapping("/search")
    @ApiOperation(value = "Search among all 'OptionProfile'", notes = "Allows to search among all 'OptionProfile'", response = OptionProfile.class, responseContainer = "Page", authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OptionProfiles successfully returned."),
            @ApiResponse(code = 400, message = "Invalid data supplied"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N), default value = O"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page, default value = 20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    +
                    "Default sort order is ascending. " +
                    "Multiple sort criteria are supported.")
    })
    @PreAuthorize("hasAuthority(@admin.getAdmin())")
    public ResponseEntity<Page<OptionProfile>> getOptionProfiles(
            Pageable pageable, @ApiParam(value = "Filters", required = true) @NotNull @RequestBody Filters filters)
            throws QualysException {

        return ResponseEntity.ok(optionProfileService.search(pageable, filters));

    }

}
