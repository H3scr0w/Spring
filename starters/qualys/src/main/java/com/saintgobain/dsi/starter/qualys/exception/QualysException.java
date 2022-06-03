package com.saintgobain.dsi.starter.qualys.exception;

/**
 * The type website4sgcore exception.
 */
public abstract class QualysException extends Exception {

    private static final long serialVersionUID = 1L;

    public QualysException() {
        super();
    }

    public QualysException(String message) {
        super(message);
    }

    public QualysException(Throwable cause) {
        super(cause);
    }
}
