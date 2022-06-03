package com.sgdbf.service.notifier.service;

import com.sgdbf.service.notifier.domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sgdbf.commons.stream.StreamUtils.streamOf;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Send.
     *
     * @param message the message
     */
    public void send(final Message message) {
        final List<String> destinations = isEmpty(message.getRecipients()) ?
                singletonList("/topic/" + message.getType()) :
                streamOf(message.getRecipients())
                        .map(user -> "/user/" + user + "/topic/" + message.getType())
                        .collect(toList());

        streamOf(destinations)
                .distinct()
                .parallel()
                .forEach(destination ->
                        this.simpMessagingTemplate.convertAndSend(destination, message.getBody()));

        log.debug("Message sent : {}", message);
    }
}
