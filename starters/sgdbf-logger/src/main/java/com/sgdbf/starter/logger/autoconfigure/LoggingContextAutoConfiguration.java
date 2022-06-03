package com.sgdbf.starter.logger.autoconfigure;

import com.sgdbf.starter.logger.properties.RequestContextLoggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

/**
 * The type Exception handling auto configuration.
 */
@Configuration
@EnableConfigurationProperties(RequestContextLoggerProperties.class)
@ComponentScan("com.sgdbf.starter.logger")
public class LoggingContextAutoConfiguration {


    @Bean
    public HttpHeadersFilter httpHeadersFilter(RequestContextLoggerProperties sgdbfResquestContextConfiguration) {
        return new HttpHeadersFilter(sgdbfResquestContextConfiguration);
    }

    @Bean
    public FilterRegistrationBean registerCommonsRequestLoggingFilter(CommonsRequestLoggingFilter filter) {
        FilterRegistrationBean reg = new FilterRegistrationBean((Filter) filter);
        reg.setOrder(1);
        return reg;
    }

    @Bean
    public FilterRegistrationBean registerRequestLogFilter(HttpHeadersFilter filter) {
        FilterRegistrationBean reg = new FilterRegistrationBean((Filter) filter);
        reg.setOrder(0);
        return reg;
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

}
