package com.sgdbf.service.notifier.service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.sgdbf.service.notifier.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;

import static com.sgdbf.service.notifier.util.Constant.BROADCAST;

@Slf4j
public class ServerNode {

    public static final long RUN_TIME_MINUTES = 2;

    public static void main(String[] args) throws Exception {
        String appName = "notifier";
        String env = "dev";
        String clusterName = "hz-" + appName + "-" + env;
        Config hazelcastConfig = new Config();
        hazelcastConfig.setClusterName(clusterName);
        hazelcastConfig.setInstanceName(clusterName);

        hazelcastConfig.getNetworkConfig()
                .setPublicAddress(InetAddress.getLocalHost().getHostAddress());
        hazelcastConfig.getNetworkConfig().setReuseAddress(true);
        hazelcastConfig.getNetworkConfig().setPort(5701);
        hazelcastConfig.getNetworkConfig().setPortCount(10);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);

        String propertyMembers = InetAddress.getLocalHost().getHostAddress()+",";


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
                            log.info("Add member IP: " + ip.getHostAddress());
                            hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig()
                                    .addMember(ip.getHostAddress());
                        }
                    }
                } catch (UnknownHostException e) {
                    log.error("Unable to find any " + member + " instance on the network", e);
                }
            }
        }

        hazelcastConfig.getNetworkConfig().getRestApiConfig().setEnabled(true);

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig);

        ITopic<Message> topic = hazelcastInstance.getTopic(BROADCAST);
        topic.addMessageListener(new MessageListenerImplTest());

        Instant start = Instant.now();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMinutes();

        while (timeElapsed < RUN_TIME_MINUTES) {
            Thread.sleep(5000);
            log.info("publish message");
            topic.publish(new Message("test", null, null));
            finish = Instant.now();
            timeElapsed = Duration.between(start, finish).toMinutes();
        }

        hazelcastInstance.shutdown();
        System.exit(0);

    }
}
