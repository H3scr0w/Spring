package com.saintgobain.dsi.pcpeg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class PcpegPropertiesResolver {
    @Value("${saint.gobain.pcpeg.group.admin}")
    private String admin;

    @Value("${saint.gobain.pcpeg.group.super-admin}")
    private String superAdmin;

    @Value("${saint.gobain.pcpeg.group.users}")
    private String users;
}
