package com.saintgobain.dsi.starter.incapsula.exception;

public class BadRequestException extends IncapsulaException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

}
