package com.sgdbf.service.notifier.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.sgdbf.service.notifier.domain.Message;
import com.sgdbf.service.notifier.util.MessageListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.sgdbf.service.notifier.util.Constant.BROADCAST;
import static com.sgdbf.starter.threading.AsyncConfiguration.WITH_CONTEXT_COPY;

/**
 * The type Notifier service.
 */
@Service
@Slf4j
public class NotifierService {

    private final ITopic<Message> topic;

    private final MessageService messageService;


    public NotifierService(MessageService messageService, HazelcastInstance sgdbfHazelcastInstance) {
        this.messageService = messageService;
        this.topic = sgdbfHazelcastInstance.getTopic(BROADCAST);
        this.topic.addMessageListener(new MessageListenerImpl(messageService));
    }

    /**
     * Send.
     *
     * @param message the message
     */
    @Async(WITH_CONTEXT_COPY)
    public void send(final Message message) {
        try {
            // send message to websocket clients
            messageService.send(message);

            // publish message to all notifier instances
            topic.publish(message);
        } catch (Exception e) {
            log.error("Hazelcast Broadcasting Error: ", e);
        }
    }

}
