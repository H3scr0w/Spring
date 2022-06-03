package com.sgdbf.service.notifier.service;

import com.hazelcast.cluster.Member;
import com.hazelcast.topic.MessageListener;
import com.sgdbf.service.notifier.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MessageListenerImplTest implements MessageListener<Message> {


    @Override
    public void onMessage(com.hazelcast.topic.Message<Message> topicMessage) {

        Member member = topicMessage.getPublishingMember();
        // check that publisher is not subscriber to avoid doublon messages publishing
        if (!member.localMember()) {
            // send message received from notifier instance to websocket clients
            log.info("receive message: " + topicMessage.getMessageObject().getType());
        }

    }
}
