package com.saintgobain.dsi.starter.elk.autoconfigure;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;

/**
 * The type Logging auto configuration.
 */
@Configuration
@EnableConfigurationProperties(SaintGobainLoggingProperties.class)
public class LoggingAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAutoConfiguration.class);

    private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private SaintGobainLoggingProperties saintGobainLoggingProperties;

    /**
     * Instantiates a new Logging auto configuration.
     *
     * @param saintGobainLoggingProperties the SaintGobain logging properties
     */
    public LoggingAutoConfiguration(SaintGobainLoggingProperties saintGobainLoggingProperties) {
        this.saintGobainLoggingProperties = saintGobainLoggingProperties;
    }

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        if (saintGobainLoggingProperties.getLogstash().isEnabled()) {
            addLogstashAppender(context);

            // Add context listener
            LogbackLoggerContextListener loggerContextListener = new LogbackLoggerContextListener();
            loggerContextListener.setContext(context);
            context.addListener(loggerContextListener);
        }
    }

    /**
     * Add logstash appender.
     *
     * @param context the context
     */
    public void addLogstashAppender(LoggerContext context) {
        LOGGER.info("Initializing Logstash logging");

        LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
        logstashAppender.setName("LOGSTASH");
        logstashAppender.setContext(context);

        Map<String, String> map = new HashMap<>();
        map.put("app", appName);
        map.put("app_port", serverPort);
        map.put("env", activeProfile);

        for (Map.Entry<String, String> entry : saintGobainLoggingProperties.getLogstash().getCustomFields().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        String collect = map.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        String customFields = "{" + collect + "}";

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setCustomFields(customFields);

        for (int i = 0; i < saintGobainLoggingProperties.getLogstash().getHost().size(); i++) {
            String host = saintGobainLoggingProperties.getLogstash().getHost().get(i);
            int port = saintGobainLoggingProperties.getLogstash().getPort().get(i);
            logstashAppender.addDestinations(new InetSocketAddress(host, port));
        }

        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setRootCauseFirst(true);
        logstashEncoder.setThrowableConverter(throwableConverter);
        logstashEncoder.setCustomFields(customFields);

        logstashAppender.setEncoder(logstashEncoder);
        logstashAppender.start();

        // Wrap the appender in an Async appender for performance
        AsyncAppender asyncLogstashAppender = new AsyncAppender();
        asyncLogstashAppender.setContext(context);
        asyncLogstashAppender.setName("ASYNC_LOGSTASH");
        asyncLogstashAppender.setQueueSize(saintGobainLoggingProperties.getLogstash().getQueueSize());
        asyncLogstashAppender.addAppender(logstashAppender);
        asyncLogstashAppender.start();

        context.getLogger("ROOT").addAppender(asyncLogstashAppender);
    }

    /**
     * Logback configuration is achieved by configuration file and API. When configuration file change is detected, the
     * configuration is reset. This listener ensures that the programmatic configuration is also re-applied after reset.
     */
    class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {

        @Override
        public boolean isResetResistant() {
            return true;
        }

        @Override
        public void onStart(LoggerContext context) {
            addLogstashAppender(context);
        }

        @Override
        public void onReset(LoggerContext context) {
            addLogstashAppender(context);
        }

        @Override
        public void onStop(LoggerContext context) {
            // nothing to do
        }

        @Override
        public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
            // nothing to do
        }
    }

}
