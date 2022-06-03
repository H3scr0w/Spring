package com.saintgobain.dsi.starter.aop.autoconfigure;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

/**
 * The type Logging aspect auto configuration.
 */
@Configuration
@ConditionalOnProperty(prefix = LoggingAspectProperties.SPRING_AOP_LOGGING_PREFIX, name = LoggingAspectProperties.ENABLED_PROPERTY, havingValue = "true")
@EnableConfigurationProperties(LoggingAspectProperties.class)
public class LoggingAspectAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspectAutoConfiguration.class);

    private LoggingAspectProperties loggingAspectProperties;

    /**
     * Instantiates a new Logging aspect auto configuration.
     *
     * @param loggingAspectProperties the logging aspect properties
     */
    public LoggingAspectAutoConfiguration(LoggingAspectProperties loggingAspectProperties) {
        this.loggingAspectProperties = loggingAspectProperties;
    }

    /**
     * Loggingdvisor aspect j expression pointcut advisor.
     *
     * @return the aspect j expression pointcut advisor
     */
    @Bean
    public AspectJExpressionPointcutAdvisor loggingdvisor() {

        String exprJoinedPackages = "";
        if (loggingAspectProperties.getBasePackages() != null) {
            exprJoinedPackages = loggingAspectProperties.getBasePackages().stream().map(basePackage -> "within("
                    + basePackage + "..*)").collect(Collectors.joining(" || "));
        }

        String exprAnnotation = "@annotation(" + SaintGobainLogging.class.getCanonicalName() + ")";

        String expression = exprJoinedPackages + (StringUtils.isEmpty(exprJoinedPackages) ? "" : " || ")
                + exprAnnotation;

        LOGGER.debug("Saint-Gobain aop logging is enabled with pointcut expression : {}", expression);

        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setExpression(expression);
        pointcutAdvisor.setAdvice(new LoggingMethodInterceptor());
        return pointcutAdvisor;

    }

    private class LoggingMethodInterceptor implements MethodInterceptor {

        private final Logger log = LoggingAspectAutoConfiguration.this.LOGGER;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (log.isDebugEnabled()) {
                log.debug("Enter: {}.{}() with argument[s] = {}", invocation.getMethod().getDeclaringClass().getName(),
                        invocation.getMethod().getName(), Arrays.toString(invocation.getArguments()));
            }
            StopWatch watch = new StopWatch();
            watch.start();
            try {
                Object result = invocation.proceed();
                watch.stop();
                if (log.isDebugEnabled()) {
                    log.debug("Exit: {}.{}() with result = {} in {}ms", invocation.getMethod().getDeclaringClass()
                            .getName(), invocation.getMethod().getName(), result, watch.getTotalTimeMillis());
                }
                return result;
            } catch (IllegalArgumentException e) {
                watch.stop();
                log.error("Illegal argument: {} in {}.{}()", Arrays.toString(invocation.getArguments()), invocation
                        .getMethod().getDeclaringClass().getName(), invocation.getMethod().getName());
                throw e;
            } catch (Exception ex) {
                handleException(invocation, ex);
                throw ex;
            }
        }

        private void handleException(MethodInvocation invocation, Exception e) {
            if (loggingAspectProperties.isFullStack()) {
                log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", invocation.getMethod()
                        .getDeclaringClass().getName(), invocation.getMethod().getName(), e.getCause() != null ? e
                                .getCause() : "NULL", e.getMessage(), e);
            } else {
                log.error("Exception in {}.{}() with cause = {}", invocation.getMethod().getDeclaringClass().getName(),
                        invocation.getMethod().getName(), e.getCause() != null ? e.getCause() : "NULL");
            }
        }

    }

}
