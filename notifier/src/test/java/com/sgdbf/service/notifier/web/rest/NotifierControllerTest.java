package com.sgdbf.service.notifier.web.rest;

import com.google.gson.Gson;
import com.sgdbf.commons.rest.SgdbfExceptionHandler;
import com.sgdbf.service.notifier.domain.Message;
import com.sgdbf.service.notifier.service.NotifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class NotifierControllerTest {

    private static final String SEND_URL = "/send";
    private final Gson gson = new Gson();

    @InjectMocks
    private NotifierController notifierController;

    @Mock
    private NotifierService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(notifierController)
                .setControllerAdvice(new SgdbfExceptionHandler(new MockEnvironment()))
                .build();
    }


    @Test
    public void should_send() throws Exception {
        // given
        String martyId = "morty_id";
        String docId = "rick_id";

        Message message = new Message("notif_type", Arrays.asList(martyId, docId), "The body content");

        // when
        ResultActions action = mockMvc.perform(
                post(SEND_URL)
                        .content(gson.toJson(message))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        // then
        action.andExpect(status().isNoContent());

        then(service).should().send(message);
    }

    @Test
    public void should_refuse_to_send_blank_message() throws Exception {
        // given
        String martyId = "morty_id";
        String docId = "rick_id";

        Message message = new Message("notif_type", Arrays.asList(martyId, docId), null);

        // when
        ResultActions action = mockMvc.perform(
                post(SEND_URL)
                        .content(gson.toJson(message))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        );
        // then
        action.andExpect(status().isBadRequest());
    }

}
