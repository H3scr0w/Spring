package com.saintgobain.dsi.pcpeg.client.directory.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.pcpeg.client.directory.model.User;
import com.saintgobain.dsi.pcpeg.client.directory.model.UserResult;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final PcpegProperties properties;
    private static final String FIELDS_PARAM = "_fields";
    private static final String FIELDS_VALUES = "stGoSGI,sn,givenName,mail,telephoneNumber,cn";

    public User findUser(String sgid) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(getUserUrl() + "/" + sgid);
        return restTemplate.getForObject(uriComponentsBuilder.toUriString(), User.class);
    }

    public List<User> findAllUsers(String sgid, String firstName, String lastName) {
        String queryFilter = "";
        if (StringUtils.isNotBlank(sgid)) {

            queryFilter = "(stGoSGI eq '" + StringUtils.upperCase(sgid) + "')";

        } else {
            if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {

                queryFilter = "(sn sw '" + StringUtils.upperCase(lastName)
                        + "' and givenName sw '" + StringUtils.capitalize(firstName) + "')";

            } else if (StringUtils.isNotBlank(firstName)) {

                queryFilter = "(givenName sw '" + StringUtils.upperCase(firstName) + "')";

            } else if (StringUtils.isNotBlank(lastName)) {

                queryFilter = "(sn sw '" + StringUtils.upperCase(lastName) + "')";

            }

        }
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(getUserUrl())
                .queryParam(FIELDS_PARAM, FIELDS_VALUES)
                .queryParam("_pageSize", 50);
        String url = uriComponentsBuilder.toUriString() + "&_queryFilter=" + queryFilter;
        UserResult result = restTemplate.getForObject(url, UserResult.class);
        return result.getResult();
    }

    private String getUserUrl() {
        return properties.getDirectory().getGroupDirectory() + "/users";
    }
}
