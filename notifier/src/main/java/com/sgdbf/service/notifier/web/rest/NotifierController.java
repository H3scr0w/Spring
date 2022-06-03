package com.sgdbf.service.notifier.web.rest;

import com.sgdbf.service.notifier.domain.Message;
import com.sgdbf.service.notifier.service.NotifierService;
import com.sgdbf.starter.aop.autoconfigure.SgdbfLogging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The type Notifier controller.
 */
@Api(value = "/", tags = "")
@RestController
@RequiredArgsConstructor
public class NotifierController {

    private final NotifierService notifierService;

    /**
     * Send response entity.
     *
     * @param message the message
     * @return the response entity
     */
    @ApiOperation(
            value = "Send a message to several recipients",
            notes = "Allows to send a message to several recipients",
            nickname = "send"
    )
    @ApiResponses(
            @ApiResponse(code = 400, message = "Api response bad request, parameters required")
    )
    @SgdbfLogging
    @PostMapping(path = "/send")
    public ResponseEntity<Void> send(@ApiParam(value = "Message to be send", required = true,
            name = "message") @RequestBody @Valid Message message) {
        notifierService.send(message);
        return ResponseEntity.noContent().build();
    }
}
