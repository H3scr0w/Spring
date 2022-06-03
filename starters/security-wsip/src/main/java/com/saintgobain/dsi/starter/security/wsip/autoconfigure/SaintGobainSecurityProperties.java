package com.saintgobain.dsi.starter.security.wsip.autoconfigure;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Saint gobain security properties.
 */
@ConfigurationProperties(prefix = "saint.gobain.security.wsip", ignoreUnknownFields = false)
public class SaintGobainSecurityProperties {

    private final Exposure exposure = new Exposure();

    private String jwtPublicKey = "jwt.public.key";

    /**
     * Gets exposure.
     *
     * @return the exposure
     */
    public Exposure getExposure() {
        return exposure;
    }

    /**
     * Get jwt public key path
     * 
     * @return
     */
    public String getJwtPublicKey() {
        return jwtPublicKey;
    }

    /**
     * Sets jwt public key path
     * 
     * @param jwtPublicKey
     */
    public void setJwtPublicKey(String jwtPublicKey) {
        if (StringUtils.isNotBlank(jwtPublicKey)) {
            this.jwtPublicKey = jwtPublicKey;
        }
    }

    /**
     * The type Exposure.
     */
    public static class Exposure {
        /**
         * Endpoint that should be excluded.
         */
        private Set<String> exclude = new LinkedHashSet<>();

        /**
         * Gets exclude.
         *
         * @return the exclude
         */
        public Set<String> getExclude() {
            return this.exclude;
        }

        /**
         * Sets exclude.
         *
         * @param exclude the exclude
         */
        public void setExclude(Set<String> exclude) {
            this.exclude = exclude;
        }
    }

}
