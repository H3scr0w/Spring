package com.saintgobain.dsi.starter.qualys.exception;

public class BadRequestException extends QualysException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

}
