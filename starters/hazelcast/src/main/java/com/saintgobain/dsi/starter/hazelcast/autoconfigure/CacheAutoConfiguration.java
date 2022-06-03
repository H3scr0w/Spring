package com.saintgobain.dsi.starter.hazelcast.autoconfigure;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * The type Cache configuration.
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    private final CacheProperties properties;

    /**
     * Instantiates a new Cache auto configuration.
     *
     * @param properties the properties
     */
    public CacheAutoConfiguration(CacheProperties properties) {
        this.properties = properties;
    }

    /**
     * Destroy.
     */
    @PreDestroy
    public void destroy() {
        LOGGER.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    /**
     * Cache manager cache manager.
     *
     * @param hazelcastInstance the hazelcast instance
     * @return the cache manager
     */
    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        LOGGER.debug("Starting HazelcastCacheManager");
        CacheManager cacheManager = new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance);
        return cacheManager;
    }

    /**
     * Hazelcast instance.
     *
     * @return the hazelcast instance
     */
    @Bean
    public HazelcastInstance hazelcastInstance() {
        LOGGER.debug("Configuring Hazelcast");
        HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName(properties.getInstanceName());
        if (hazelCastInstance != null) {
            LOGGER.debug("Hazelcast already initialized");
            return hazelCastInstance;
        }
        Config config = new Config();
        config.setInstanceName(properties.getInstanceName());
        config.getNetworkConfig().setPort(properties.getPort());
        config.getNetworkConfig().setPortAutoIncrement(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        if (properties.getMembers() != null && !properties.getMembers().isEmpty()) {
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
            properties.getMembers().forEach(member -> config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(member));
        }
        config.getMapConfigs().put("default", initializeDefaultMapConfig());

        // Full reference is available at:
        // http://docs.hazelcast.org/docs/management-center/3.9/manual/html/Deploying_and_Starting.html
        config.setManagementCenterConfig(initializeDefaultManagementCenterConfig());
        config.getMapConfigs().put(properties.getPackageName(), initializeDomainMapConfig());
        return Hazelcast.newHazelcastInstance(config);
    }

    private ManagementCenterConfig initializeDefaultManagementCenterConfig() {
        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
        managementCenterConfig.setEnabled(properties.getManagementCenter().isEnabled());
        managementCenterConfig.setUrl(properties.getManagementCenter().getUrl());
        managementCenterConfig.setUpdateInterval(properties.getManagementCenter().getUpdateInterval());
        return managementCenterConfig;
    }

    private MapConfig initializeDefaultMapConfig() {
        MapConfig mapConfig = new MapConfig();

        /*
         * Number of backups. If 1 is set as the backup-count for example, then all entries of the map will be copied to
         * another JVM for fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
         */
        mapConfig.setBackupCount(properties.getBackupCount());

        /*
         * Valid values are: NONE (no eviction), LRU (Least Recently Used), LFU (Least Frequently Used). NONE is the
         * default.
         */
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

        /*
         * Maximum size of the map. When max size is reached, map is evicted based on the policy defined. Any integer
         * between 0 and Integer.MAX_VALUE. 0 means Integer.MAX_VALUE. Default is 0.
         */
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

        return mapConfig;
    }

    private MapConfig initializeDomainMapConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(properties.getTimeToLiveSeconds());
        return mapConfig;
    }
}
