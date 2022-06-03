package com.saintgobain.dsi.pcpeg.client.directory.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.pcpeg.client.directory.model.Group;
import com.saintgobain.dsi.pcpeg.client.directory.model.RestOperation;
import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final RestTemplate restTemplate;
    private final PcpegProperties properties;
    private final UserService userService;

    public Group addUserInGroup(String sgid) throws PcpegException {

        // Check user exists in GD
        checkUserInDirectory(sgid);

        // Check group exists in GD
        Group group = checkUserGroupInDirectory();

        List<String> uniqueMembers = group.getUniqueMember();

        // No need to go further if user is already in group
        if (uniqueMembers.stream().anyMatch(member -> StringUtils.containsIgnoreCase(member, sgid))) {
            return group;
        }

        String newUniqueMember = String.format(
                Constants.REST_UNIQUE_MEMBER_VALUE,
                sgid);
        uniqueMembers.add(newUniqueMember);

        group.setUniqueMember(uniqueMembers);

        return updateUniqueMembersGroup(group);
    }

    public Group deleteUserInGroup(String sgid) throws PcpegException {

        // Check user exists in GD
        checkUserInDirectory(sgid);

        // Check group exists in GD
        Group group = checkUserGroupInDirectory();

        List<String> uniqueMembers = group.getUniqueMember();

        uniqueMembers = uniqueMembers.stream().filter(member -> !StringUtils.containsIgnoreCase(member,
                sgid)).collect(
                        Collectors.toList());

        group.setUniqueMember(uniqueMembers);

        return updateUniqueMembersGroup(group);
    }

    public Group replaceUserInGroup(String oldSgid, String newSgid) throws PcpegException {

        // Check users exists in GD
        checkUserInDirectory(oldSgid);
        checkUserInDirectory(newSgid);

        // Check group exists in GD
        Group group = checkUserGroupInDirectory();

        List<String> uniqueMembers = group.getUniqueMember();

        // Add new user in group if not exist
        if (uniqueMembers.stream().noneMatch(member -> StringUtils.containsIgnoreCase(member, newSgid))) {
            String newUniqueMember = String.format(
                    Constants.REST_UNIQUE_MEMBER_VALUE,
                    newSgid);
            uniqueMembers.add(newUniqueMember);
        }

        // Delete old user in group if exist
        uniqueMembers = uniqueMembers.stream().filter(member -> !StringUtils.containsIgnoreCase(member,
                oldSgid)).collect(
                        Collectors.toList());

        group.setUniqueMember(uniqueMembers);

        return updateUniqueMembersGroup(group);
    }

    private Group updateUniqueMembersGroup(Group group) {

        Group newGroup;
        RestOperation restOperation = RestOperation.builder()
                .field("uniqueMember")
                .operation(Constants.REST_OPERATION_REPLACE)
                .value(group.getUniqueMember())
                .build();
        List<RestOperation> restOperations = Arrays.asList(restOperation);
        String url = getGroupUrl() + "/" + group.getId();
        newGroup = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(restOperations),
                Group.class).getBody();

        return newGroup;
    }

    private void checkUserInDirectory(String sgid) {

        try {
            userService.findUser(sgid);
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("User " + sgid + " not found");
        }
    }

    private Group checkUserGroupInDirectory() {
        Group group = null;
        try {
            group = findGroupByCn(properties.getGroup().getUsers());
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("Group " + properties.getGroup().getUsers() + " not found");
        }

        return group;
    }

    private Group findGroupByCn(String cn) {
        var url = getGroupUrl() + "/" + cn;
        return restTemplate.getForObject(url, Group.class);
    }

    private String getGroupUrl() {
        return properties.getDirectory().getGroupManagement() + "/groups";
    }

}
