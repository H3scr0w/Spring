package com.saintgobain.dsi.pcpeg.web.errors;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;

/**
 * The type Error handler.
 */
@RestControllerAdvice
public class ErrorHandler implements ProblemHandling, SecurityAdviceTrait {

    /**
     * Handle pcpeg exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(PcpegException.class)
    public ResponseEntity<Problem> handlePcpegException(PcpegException e, NativeWebRequest request) {
        return create(Status.BAD_REQUEST, e, request);
    }

    /**
     * Handler NOT_FOUND exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Problem> handlerException(EntityNotFoundException e, NativeWebRequest request) {
        return create(Status.NOT_FOUND, e, request);
    }

    /**
     * Handle exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(Exception e, NativeWebRequest request) {
        return create(Status.INTERNAL_SERVER_ERROR, e, request);
    }

    /**
     * Handler exception.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Problem> handlerException(MaxUploadSizeExceededException e, NativeWebRequest request) {
        if (e.getRootCause() != null) {
            return create(Status.BAD_REQUEST, e.getRootCause(), request);
        }
        return create(Status.BAD_REQUEST, e, request);
    }

    /**
     * Handler Rest Client exception response entity.
     *
     * @param e the e
     * @param response the response
     * @throws IOException the io exception
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public void handlerException(HttpClientErrorException e, HttpServletResponse response) throws IOException {
        JsonNode errorBody = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
        response.sendError(e.getStatusCode().value(), errorBody.get("detail").asText());
    }
}
