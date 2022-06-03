package com.saintgobain.dsi.starter.incapsula.exception;

/**
 * The type website4sgcore exception.
 */
public abstract class IncapsulaException extends Exception {

    private static final long serialVersionUID = 1L;

    public IncapsulaException() {
        super();
    }

    public IncapsulaException(String message) {
        super(message);
    }

    public IncapsulaException(Throwable cause) {
        super(cause);
    }
}
