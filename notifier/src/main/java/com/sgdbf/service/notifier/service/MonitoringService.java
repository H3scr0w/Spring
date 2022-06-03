package com.sgdbf.service.notifier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.topic.ITopic;
import com.sgdbf.service.notifier.NotifierApplication;
import com.sgdbf.service.notifier.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import java.util.ArrayList;
import java.util.List;

import static com.sgdbf.commons.stream.StreamUtils.streamOf;
import static com.sgdbf.service.notifier.util.Constant.BROADCAST;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

    private static final List<Integer> dailyCurrentConnections = new ArrayList<>();
    private static final List<Integer> thirtyMinutesCurrentConnections = new ArrayList<>();

    private final WebSocketMessageBrokerStats webSocketMessageBrokerStats;
    private final ObjectMapper objectMapper;
    private final HazelcastInstance sgdbfHazelcastInstance;

    @Scheduled(cron = "${scheduled-tasks.web-socket-two-minutes-connections.cron}")
    public void getTwoMinutesConnections() {
        int currentConnections = extractCurrentConnection();
        dailyCurrentConnections.add(currentConnections);
        thirtyMinutesCurrentConnections.add(currentConnections);
    }

    @Scheduled(cron = "${scheduled-tasks.web-socket-thirty-minutes-connections.cron}")
    public void logThirtyMinutesConnections() throws JsonProcessingException {
        log.info("Web socket message broker stats\n{}", objectMapper.writeValueAsString(webSocketMessageBrokerStats));
        logStats(thirtyMinutesCurrentConnections, "30MIN");
    }

    @Scheduled(cron = "${scheduled-tasks.web-socket-connections-stats.cron}")
    public void logWebSocketStats() {
        logStats(dailyCurrentConnections, "1DAY");
    }

    @Scheduled(cron = "${scheduled-tasks.check-hazelcast-instance.cron}")
    public void checkHazelcastInstance() {
        try {
            sgdbfHazelcastInstance.getTopic(BROADCAST);
        } catch (HazelcastInstanceNotActiveException hzEx) {
            log.error("Hazelcast Instance out of memory", hzEx);
            NotifierApplication.restart();
        }

    }

    private void logStats(List<Integer> currentConnections, String periodDescription) {
        if (isNotEmpty(currentConnections)) {
            ITopic<Message> topic = sgdbfHazelcastInstance.getTopic(BROADCAST);
            long publishOperationCount = topic.getLocalTopicStats().getPublishOperationCount();
            long receiveOperationCount = topic.getLocalTopicStats().getReceiveOperationCount();

            int maxWebSocketConnectionsCount = streamOf(currentConnections).max(Integer::compare).orElse(0);
            int minWebSocketConnectionsCount = streamOf(currentConnections).min(Integer::compare).orElse(0);
            int averageConnectionsCount = streamOf(currentConnections)
                    .mapToInt(Integer::intValue)
                    .sum() / currentConnections.size();

            MDC.put("maxWebSocketConnections", Integer.toString(maxWebSocketConnectionsCount));
            MDC.put("minWebSocketConnections", Integer.toString(minWebSocketConnectionsCount));
            MDC.put("averageWebSocketConnections", Integer.toString(averageConnectionsCount));
            MDC.put("webSocketConnectionsMonitoringType", periodDescription);
            MDC.put("publishOperationCount", Long.toString(publishOperationCount));
            MDC.put("receiveOperationCount", Long.toString(receiveOperationCount));
            log.info(
                    "Websocket connections stats over " + periodDescription + "\n" +
                            "Maximum web socket connections: " + maxWebSocketConnectionsCount + "\n" +
                            "Minimum web socket connections: " + minWebSocketConnectionsCount + "\n" +
                            "Average web socket connections: " + averageConnectionsCount + "\n" +
                            "Broadcast publish messages: " + publishOperationCount + "\n" +
                            "Broadcast receive messages: " + receiveOperationCount);
            MDC.remove("maxWebSocketConnections");
            MDC.remove("minWebSocketConnections");
            MDC.remove("averageWebSocketConnections");
            MDC.remove("webSocketConnectionsMonitoringType");
            MDC.remove("publishOperationCount");
            MDC.remove("receiveOperationCount");
            currentConnections.clear();
        }
    }

    private int extractCurrentConnection() {
        return Integer.parseInt(webSocketMessageBrokerStats.getWebSocketSessionStatsInfo().split(" ")[0]);
    }
}
