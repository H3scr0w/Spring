package com.sgdbf.service.notifier.service;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
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
public class ClientNode {

    public static final long RUN_TIME_MINUTES = 2;

    public static void main(String[] args) throws Exception {
        String clusterName = "hz-notifier-atlas-sbx1-int";
        ClientConfig config = new ClientConfig();
        config.setClusterName(clusterName);
        config.getNetworkConfig().addAddress("ingress.ibm.ppr.docker4sg.saint-gobain.net:20007");

        HazelcastInstance hazelcastInstanceClient = HazelcastClient.newHazelcastClient(config);

        ITopic<Message> topic = hazelcastInstanceClient.getTopic(BROADCAST);
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

        hazelcastInstanceClient.shutdown();
        System.exit(0);

    }


}
