package com.saintgobain.dsi.pcpeg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * The type Experience properties.
 */
@Data
@ConfigurationProperties(prefix = "saint.gobain.pcpeg")
public class PcpegProperties {
    private final Directory directory = new Directory();

    private final Group group = new Group();

    private final Mail mail = new Mail();

    private String commonLocation;

    private String frontendUrl;

    @Data
    public class Mail {
        private boolean enabled;
        private String from;
        private String invitationSubject;
    }

    @Data
    public static class Group {
        private String admin;
        private String superAdmin;
        private String users;
    }

    @Data
    public class Directory {
        private String url;
        private String keyId;
        private String groupManagement;
        private String groupDirectory;
    }
}
