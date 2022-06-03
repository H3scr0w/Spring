package com.sgdbf.service.notifier.service;

import com.hazelcast.core.HazelcastInstance;
import com.sgdbf.service.notifier.domain.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotifierServiceTest {

    private NotifierService notifierService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private HazelcastInstance sgdbfHazelcastInstance;

    private MessageService messageService;

    @Before
    public void beforeEach(){

        reset(messagingTemplate);
        messageService = new MessageService(messagingTemplate);
        notifierService = new NotifierService(messageService, sgdbfHazelcastInstance);
    }

    @Test
    public void should_send_message() {
        // given
        String martyId = "marty_id";
        String docId = "doc_id";
        Message message = new Message("notif_type", asList(martyId, docId), "The body content");
        // when
        notifierService.send(message);
        // then
        verify(messagingTemplate, times(2)).convertAndSend(any(String.class), eq(message.getBody()));
        verify(messagingTemplate, times(1)).convertAndSend(eq(format("/user/%s/topic/%s", martyId, message.getType())), eq(message.getBody()));
        verify(messagingTemplate, times(1)).convertAndSend(eq(format("/user/%s/topic/%s", docId, message.getType())), eq(message.getBody()));

    }

    @Test
    public void should_send_message_braodcast_endpoint_no_reciepient_empty_list() {
        // given
        Message message = new Message("notif_type", emptyList(), "The body content");
        // when
        notifierService.send(message);
        // then
        verify(messagingTemplate, times(1)).convertAndSend(eq(format("/topic/%s", message.getType())), eq(message.getBody()));
    }

    @Test
    public void should_send_message_braodcast_endpoint_no_reciepient_null_list() {
        // given
        Message message = new Message("notif_type", null, "The body content");
        // when
        notifierService.send(message);
        // then
        verify(messagingTemplate, times(1)).convertAndSend(eq(format("/topic/%s", message.getType())), eq(message.getBody()));
    }

}
