package com.saintgobain.dsi.pcpeg.web.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saintgobain.dsi.pcpeg.client.directory.model.User;
import com.saintgobain.dsi.pcpeg.client.directory.service.UserService;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(value = "/directory-users")
@RequestMapping("/directory-users")
@RestController
@RequiredArgsConstructor
public class UserDirectoryEndpoint {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Get all directory users", response = User.class, authorizations = {
            @Authorization(value = "jwt") })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Successfully completed"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Error") })
    @PreAuthorize("hasAnyRole("
            + "@pcpegPropertiesResolver.admin,"
            + "@pcpegPropertiesResolver.superAdmin,"
            + "@pcpegPropertiesResolver.users)")
    public ResponseEntity<List<User>> getAllDirectoryUsers(
            @ApiParam(value = "sgid", required = true) @RequestParam(value = "sgid", required = false) String sgid,
            @ApiParam(value = "firstName", required = true) @RequestParam(value = "firstName", required = false) String firstName,
            @ApiParam(value = "lastName", required = true) @RequestParam(value = "lastName", required = false) String lastName)
            throws PcpegException {
        return ResponseEntity.ok(userService.findAllUsers(sgid, firstName, lastName));
    }

}
