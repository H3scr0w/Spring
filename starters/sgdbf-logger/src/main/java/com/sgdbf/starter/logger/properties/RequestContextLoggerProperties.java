package com.sgdbf.starter.logger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * The type Sgdbf properties.
 */
@ConfigurationProperties(prefix = RequestContextLoggerProperties.SGDBF_PREFIX, ignoreUnknownFields = false)
public class RequestContextLoggerProperties {

    /**
     * The constant SGDBF_PREFIX.
     */
    protected static final String SGDBF_PREFIX = "sgdbf.request.context.log";


    private final Set<String> headers = new HashSet<>(asList(
            "x-correlationid",
            "atlas-request-id",
            "request-id",
            "x-request-id",
            "user-agent",
            "x-forwarded-host"));


    private static Set<String> toHeaderNameSet(final String headerNamesStr) {
        return toHeaderNameSet(headerNamesStr.split(","));
    }

    private static Set<String> toHeaderNameSet(final String ... headerNamesTab) {
        return Stream.of(headerNamesTab)
                .map(header -> header.trim().toLowerCase())
                .collect(Collectors.toSet());
    }

    /**
     * Gets sgdbf prefix.
     *
     * @return the sgdbf prefix
     */
    public static String getSgdbfPrefix() {
        return SGDBF_PREFIX;
    }

    /**
     * List of source headers to log.
     * default: authorization, atlas-request-id,
     * @param headers
     */
    public void setHeaders(final String headers) {
        this.headers.addAll(toHeaderNameSet(headers));
    }

    /**
     * List of source headers to log.
     *
     * @return
     */
    public String getHeaders() {
        return String.join(",", headers.stream().toArray(String[]::new));
    }

    /**
     * List of source headers to log.
     *
     * @return
     */
    public Set<String> getHeadersSet() {
        return headers;
    }
}
