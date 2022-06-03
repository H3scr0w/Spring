package com.saintgobain.dsi.website4sg.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Admin {

    @Value("${saint.gobain.group.admin}")
    private String admin;

}
