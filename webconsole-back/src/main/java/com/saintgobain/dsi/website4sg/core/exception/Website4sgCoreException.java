package com.saintgobain.dsi.website4sg.core.exception;

/**
 * The type website4sgcore exception.
 */
public abstract class Website4sgCoreException extends Exception {

    private static final long serialVersionUID = 1L;

    public Website4sgCoreException() {
        super();
    }

    public Website4sgCoreException(String message) {
        super(message);
    }

    public Website4sgCoreException(Throwable cause) {
        super(cause);
    }
}
