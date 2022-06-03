package com.sgdbf.service.notifier.util;

import com.hazelcast.cluster.Member;
import com.hazelcast.topic.MessageListener;
import com.sgdbf.service.notifier.domain.Message;
import com.sgdbf.service.notifier.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MessageListenerImpl implements MessageListener<Message> {

    private final MessageService messageService;

    @Override
    public void onMessage(com.hazelcast.topic.Message<Message> topicMessage) {
        Member member = topicMessage.getPublishingMember();
        // check that publisher is not subscriber to avoid doublon messages publishing
        if (!member.localMember()) {
            // send message received from notifier instance to websocket clients
            messageService.send(topicMessage.getMessageObject());
        }
    }
}
