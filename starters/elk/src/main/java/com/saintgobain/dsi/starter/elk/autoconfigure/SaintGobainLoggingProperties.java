package com.saintgobain.dsi.starter.elk.autoconfigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type SaintGobain properties.
 */
@ConfigurationProperties(prefix = "saint.gobain.logging", ignoreUnknownFields = false)
public class SaintGobainLoggingProperties {

    /**
     * Forward logs to logstash over a socket, used by LoggingConfiguration
     */
    private final Logstash logstash = new Logstash();

    /**
     * Gets logstash.
     *
     * @return the logstash
     */
    public Logstash getLogstash() {
        return logstash;
    }

    /**
     * The type Logstash.
     */
    public static class Logstash {

        /**
         * Enable or disable sending log to ELK
         */
        private boolean enabled = false;

        /**
         * URL of Logstash server
         */
        private List<String> host = new ArrayList<>();

        /**
         * Port of Logstash server
         */
        private List<Integer> port = new ArrayList<>();

        /**
         * Log size
         */
        private int queueSize = 512;

        /**
         * Custom attributes to send to ELK
         */
        private final Map<String, String> customFields = new HashMap<>();

        /**
         * Is enabled boolean.
         *
         * @return the boolean
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets enabled.
         *
         * @param enabled the enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Gets host.
         *
         * @return the host
         */
        public List<String> getHost() {
            return host;
        }

        /**
         * Sets host.
         *
         * @param host the host
         */
        public void setHost(List<String> host) {
            this.host = host;
        }

        /**
         * Gets port.
         *
         * @return the port
         */
        public List<Integer> getPort() {
            return port;
        }

        /**
         * Sets port.
         *
         * @param port the port
         */
        public void setPort(List<Integer> port) {
            this.port = port;
        }

        /**
         * Gets queue size.
         *
         * @return the queue size
         */
        public int getQueueSize() {
            return queueSize;
        }

        /**
         * Sets queue size.
         *
         * @param queueSize the queue size
         */
        public void setQueueSize(int queueSize) {
            this.queueSize = queueSize;
        }

        /**
         * Gets custom fields.
         *
         * @return the custom fields
         */
        public Map<String, String> getCustomFields() {
            return customFields;
        }
    }
}
