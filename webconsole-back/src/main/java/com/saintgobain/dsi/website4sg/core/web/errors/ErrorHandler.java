package com.saintgobain.dsi.website4sg.core.web.errors;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.website4sg.core.exception.BadRequestException;
import com.saintgobain.dsi.website4sg.core.exception.ForbiddenException;
import com.saintgobain.dsi.website4sg.core.exception.NotImplementedException;

/**
 * The type Error handler.
 */
@RestControllerAdvice
public class ErrorHandler implements ProblemHandling, SecurityAdviceTrait {

    /**
     * Handler FORBIDDEN exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Problem> handlerException(ForbiddenException e, NativeWebRequest request) {
        return create(Status.FORBIDDEN, e, request);
    }

    /**
     * Handler BAD_REQUEST exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Problem> handlerException(BadRequestException e, NativeWebRequest request) {
        return create(Status.BAD_REQUEST, e, request);
    }

    /**
     * Handler QUALYS exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(QualysException.class)
    public ResponseEntity<Problem> handlerException(QualysException e, NativeWebRequest request) {
        return create(Status.BAD_REQUEST, e, request);
    }

    /**
     * Handler Incapsula exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(IncapsulaException.class)
    public ResponseEntity<Problem> handlerException(IncapsulaException e, NativeWebRequest request) {
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
     * Handler NOT_IMPLEMENTED exception response entity.
     *
     * @param e the e
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<Problem> handlerException(NotImplementedException e, NativeWebRequest request) {
        return create(Status.NOT_IMPLEMENTED, e, request);
    }

    /**
     * Handler Rest Client exception response entity.
     *
     * @param e the e
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public void handlerException(HttpClientErrorException e, HttpServletResponse response) throws IOException {
        JsonNode errorBody = new ObjectMapper().readValue(e.getResponseBodyAsString(), JsonNode.class);
        response.sendError(e.getStatusCode().value(), errorBody.get("detail").asText());
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
}
