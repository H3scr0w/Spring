package com.saintgobain.dsi.pcpeg.exception;

/**
 * The type pcpeg exception.
 */
public abstract class PcpegException extends Exception {

    /**
     * Instantiates a new pcpeg exception.
     *
     * @param s the s
     */
    public PcpegException(String s) {
        super(s);
    }

    public PcpegException() {
        super();
    }
}
