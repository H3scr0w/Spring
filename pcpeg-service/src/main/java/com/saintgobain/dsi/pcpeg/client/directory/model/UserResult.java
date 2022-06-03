package com.saintgobain.dsi.pcpeg.client.directory.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResult {
    private List<User> result;
    private int resultCount;
}
