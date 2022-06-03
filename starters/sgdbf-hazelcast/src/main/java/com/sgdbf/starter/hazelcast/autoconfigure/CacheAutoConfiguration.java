package com.sgdbf.starter.hazelcast.autoconfigure;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The type Cache configuration.
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    private final Environment environment;

    private final CacheProperties properties;

    public CacheAutoConfiguration(Environment environment,
                                  CacheProperties properties) {

        this.environment = environment;
        this.properties = properties;
    }

    /**
     * Destroy.
     */
    @PreDestroy
    public void destroy() {
        LOGGER.info("Closing Hazelcast Cluster");
        Hazelcast.shutdownAll();
    }

    /**
     * Hazelcast instance hazelcast instance.
     *
     * @return the hazelcast instance
     */
    @Bean
    public HazelcastInstance sgdbfHazelcastInstance() throws UnknownHostException {
        LOGGER.debug("Configuring Hazelcast");
        String env = environment.getProperty("info.env");
        String instanceName = "hz-" + properties.getClusterName() + "-" + env;
        String propertyMembers = properties.getMembers();
        HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName(instanceName);

        if (hazelCastInstance != null) {
            LOGGER.debug("Hazelcast already initialized");
            return hazelCastInstance;
        }

        Config hazelcastConfig = new Config();
        hazelcastConfig.setClusterName(instanceName);
        hazelcastConfig.setInstanceName(instanceName);

        hazelcastConfig.getNetworkConfig()
                .setPublicAddress(InetAddress.getLocalHost().getHostAddress() + ":" + properties.getPort());
        hazelcastConfig.getNetworkConfig().setReuseAddress(true);
        hazelcastConfig.getNetworkConfig().setPort(properties.getPort());
        hazelcastConfig.getNetworkConfig().setPortCount(properties.getPortCount());
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        if (StringUtils.isNotBlank(propertyMembers)
                && propertyMembers.contains(",")) {

            String[] members = propertyMembers.replace(" ", "").split(",");
            hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);

            for (String member : members) {
                try {
                    // Retrieve Instances IPs from the swarm service name
                    InetAddress[] ips = {};
                    ips = InetAddress.getAllByName(member);

                    // Add ips to cluster
                    if (ips.length > 0) {
                        for (InetAddress ip : ips) {
                            LOGGER.info("Add member IP: " + ip.getHostAddress());
                            hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig()
                                    .addMember(ip.getHostAddress());
                        }
                    }
                } catch (UnknownHostException e) {
                    LOGGER.error("Unable to find any " + member + " instance on the network", e);
                }
            }
        }

        hazelcastConfig.getNetworkConfig().getRestApiConfig().setEnabled(true);
        return Hazelcast.newHazelcastInstance(hazelcastConfig);
    }
}
