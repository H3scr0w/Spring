package com.saintgobain.dsi.starter.hazelcast.autoconfigure;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Cache properties.
 */
@ConfigurationProperties(prefix = "saint.gobain.cache.hazelcast")
public class CacheProperties {

    /**
     * Name of Hazelcast instance
     */
    private String instanceName = "product";

    /**
     * Package name for config.
     */
    private String packageName = "com.saintgobain.dsi.product.*";

    /**
     * By default objects stay 1 hour in the cache
     */
    private int timeToLiveSeconds = 3600;

    /**
     * Number of backups. If 1 is set as the backup-count for example, then all entries of the map will be copied to
     * another JVM for fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
     */
    private int backupCount = 1;

    /**
     * List of Hazelcast members
     */
    private List<String> members;

    /**
     * Hazelcast port
     */
    private int port = 5701;

    /**
     * Hazelcast Management Center
     */
    private final ManagementCenter managementCenter = new ManagementCenter();

    /**
     * The type Management center.
     */
    public static class ManagementCenter {

        /**
         * Hazelcast management center is disabled by default
         */
        private boolean enabled = false;

        /**
         * Updates are sent to the Hazelcast management center every 3 seconds by default
         */
        private int updateInterval = 3;

        /**
         * Default URL for Hazelcast management center
         */
        private String url = "http://localhost:8180/mancenter";

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
         * Gets update interval.
         *
         * @return the update interval
         */
        public int getUpdateInterval() {
            return updateInterval;
        }

        /**
         * Sets update interval.
         *
         * @param updateInterval the update interval
         */
        public void setUpdateInterval(int updateInterval) {
            this.updateInterval = updateInterval;
        }

        /**
         * Gets url.
         *
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets url.
         *
         * @param url the url
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * Gets instance name.
     *
     * @return the instance name
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Sets instance name.
     *
     * @param instanceName the instance name
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets time to live seconds.
     *
     * @return the time to live seconds
     */
    public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    /**
     * Sets time to live seconds.
     *
     * @param timeToLiveSeconds the time to live seconds
     */
    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    /**
     * Gets backup count.
     *
     * @return the backup count
     */
    public int getBackupCount() {
        return backupCount;
    }

    /**
     * Sets backup count.
     *
     * @param backupCount the backup count
     */
    public void setBackupCount(int backupCount) {
        this.backupCount = backupCount;
    }

    /**
     * Gets management center.
     *
     * @return the management center
     */
    public ManagementCenter getManagementCenter() {
        return managementCenter;
    }

    /**
     * Gets members.
     *
     * @return the members
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Sets members.
     *
     * @param members the members
     */
    public void setMembers(List<String> members) {
        this.members = members;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }
}
