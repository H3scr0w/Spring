package com.saintgobain.dsi.starter.security.wsip.exception;

/**
 * The type Security exception.
 */
public class SaintGobainSecurityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Security exception.
     *
     * @param s the s
     */
    public SaintGobainSecurityException(String s) {
        super(s);
    }

    /**
     * Instantiates a new Saint gobain security exception.
     *
     * @param s the s
     * @param throwable the throwable
     */
    public SaintGobainSecurityException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
