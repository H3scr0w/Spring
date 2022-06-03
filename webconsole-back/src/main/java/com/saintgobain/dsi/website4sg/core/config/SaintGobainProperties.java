package com.saintgobain.dsi.website4sg.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * The type Saint gobain properties.
 */
@Data
@ConfigurationProperties(prefix = "saint.gobain")
public class SaintGobainProperties {

    private final Api api = new Api();

    @Data
    public class Api {

        private final Rundeck rundeck = new Rundeck();
        private final Deployment deployment = new Deployment();
        private final Nexus nexus = new Nexus();
        private final OpenDj openDj = new OpenDj();
        private final Acquia acquia = new Acquia();

        @Data
        public class Rundeck {
            private String url;
            private String token;
        }

        @Data
        public class Deployment {
            private String url;
        }

        @Data
        public class Nexus {
            private String url;
            private String basicHeader;
        }

        @Data
        public class OpenDj {
            private String path;
            private String basicHeader;
        }

        @Data
        public class Acquia {
            private String tokenUrl;
            private String apiUrl;
            private String apiKey;
            private String apiSecret;
        }

    }

}
