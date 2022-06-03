package com.sgdbf.starter.logger.autoconfigure;

import com.sgdbf.starter.logger.properties.RequestContextLoggerProperties;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.list;

/**
 * Load all http header into
 */
public class HttpHeadersFilter extends GenericFilterBean {
    private final Set<String> headers;

    public HttpHeadersFilter(final RequestContextLoggerProperties sgdbfResquestContextConfiguration) {
        headers = sgdbfResquestContextConfiguration.getHeadersSet().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        list(httpReq.getHeaderNames()).stream()
                .filter(headers::contains)
                .forEach(headerName ->
                        MDC.put(headerName.toLowerCase(), httpReq.getHeaders(headerName).nextElement())
                );
        chain.doFilter(request, response);
    }
}
