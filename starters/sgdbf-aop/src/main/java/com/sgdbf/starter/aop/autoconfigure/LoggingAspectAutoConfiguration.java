package com.sgdbf.starter.aop.autoconfigure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isBlank;

/**
 * The type Logging aspect auto configuration.
 */
@Configuration
@ConditionalOnProperty(prefix = LoggingAspectProperties.SPRING_AOP_LOGGING_PREFIX,
        name = LoggingAspectProperties.ENABLED_PROPERTY, havingValue = "true")
@EnableConfigurationProperties(LoggingAspectProperties.class)
public class LoggingAspectAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspectAutoConfiguration.class);

    private static final String CLIENT_IP = "clientIp";
    private static final String CLIENT_HOST = "clientHost";
    private static final String ARGUMENTS = "arguments";
    private static final String HEADERS = "headers";
    private static final String HTTP_STATUS_CODE = "httpStatusCode";
    private static final String URL = "url";
    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String DURATION = "duration";
    private static final String ENTER_S_S = "Enter: %s.%s()";
    private static final String EXIT_S_S = "Exit: %s.%s()";
    private static final String ILLEGAL_ARGUMENT_IN_S_S_S = "Illegal argument in %s.%s(): %s";
    private static final String EXCEPTION_IN_S_S_WITH_CAUSE_S = "Exception in %s.%s() with cause = %s";
    private static final String RESULT = "result";
    private static final String ELLIPSIS = "...";
    private static final String NULL = "NULL";
    private static final String EXCEPTION = "Exception";
    private static final String LOG_POINTCUT = "SGDBF aop logging is enabled with pointcut expression : {}";

    /**
     * Loggingdvisor aspect j expression pointcut advisor.
     * @return the aspect j expression pointcut advisor
     */
    @Bean
    public AspectJExpressionPointcutAdvisor loggingAdvisor(LoggingAspectProperties loggingAspectProperties) {
        String exprJoined = "";
        if (loggingAspectProperties.getBasePackages() != null) {
            exprJoined = loggingAspectProperties.getBasePackages().stream()
                    .map(basePackage -> "within(" + basePackage + "..*)")
                    .collect(Collectors.joining(" || "));
        }
        String exprAnnotation = "@annotation(" + SgdbfLogging.class.getCanonicalName() + ")";
        String expression = exprJoined + (isBlank(exprJoined) ? "" : " || ") + exprAnnotation;
        LOGGER.debug(LOG_POINTCUT, expression);
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setExpression(expression);
        pointcutAdvisor.setAdvice(new LoggingMethodInterceptorSingleton());
        return pointcutAdvisor;
    }

    private class LoggingMethodInterceptorSingleton implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            return new LoggingMethodInterceptor().invoke(methodInvocation);
        }
    }

    private class LoggingMethodInterceptor implements MethodInterceptor {
        private final Logger log = LOGGER;
        private Instant before;
        private HttpServletRequest srcReq;
        private HttpServletResponse srcResp;
        private String className;
        private String methodName;
        private String arguments;
        private SgdbfLogging sgdbfLogging;
        private Map<String, List<String>> headersMap;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            initFields(invocation);
            logBefore();
            try {
                Object result = invocation.proceed();
                logAfter(result);
                return result;
            } catch (IllegalArgumentException e) {
                logError(e, arguments, ILLEGAL_ARGUMENT_IN_S_S_S);
                throw e;
            } catch (Exception ex) {
                String cause = ( ex.getCause() != null ? ex.getCause().toString() : NULL);
                logError(ex, cause, EXCEPTION_IN_S_S_WITH_CAUSE_S);
                throw ex;
            }
        }

        private void initFields(MethodInvocation invocation) {
            this.before = Instant.now();
            this.className = invocation.getMethod().getDeclaringClass().getName();
            this.methodName = invocation.getMethod().getName();
            this.arguments = Arrays.toString(invocation.getArguments());
            this.sgdbfLogging = invocation.getMethod().getAnnotation(SgdbfLogging.class);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if( requestAttributes != null ) {
                this.srcReq = requestAttributes.getRequest();
                this.srcResp = requestAttributes.getResponse();
                this.headersMap = Collections
                        .list(srcReq.getHeaderNames())
                        .stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                h -> Collections.list(srcReq.getHeaders(h))
                        ));
            }
        }

        private void logBefore() {
            MDC.put(CLASS_NAME, className);
            MDC.put(METHOD_NAME, methodName);
            MDC.put(ARGUMENTS, arguments);
            if( headersMap != null ) {
                MDC.put(HEADERS, headersMap.toString());
            }
            if( srcReq != null && srcReq.getRemoteAddr() != null ) {
                MDC.put(CLIENT_IP, srcReq.getRemoteAddr());
            }
            if( srcReq != null && srcReq.getRemoteHost() != null ) {
                MDC.put(CLIENT_HOST, srcReq.getRemoteHost());
            }
            if( srcReq != null && srcReq.getScheme() != null && srcReq.getServerName() != null && srcReq.getRequestURL() != null ) {
                MDC.put(URL, srcReq.getRequestURL().toString());
            }

            // log the enter message
            String messageEnter = String.format(ENTER_S_S, className, methodName);
            log.info( messageEnter);
            MDC.remove(HEADERS);
            MDC.remove(ARGUMENTS);
        }

        private void logAfter(Object result) throws JsonProcessingException {
            // after method invocation put response status code, result and duration
            if( srcResp != null ) {
                MDC.put(HTTP_STATUS_CODE, String.valueOf(this.srcResp.getStatus()));
            }
            if( sgdbfLogging != null && result != null && sgdbfLogging.result() ) {
                String jsonResult;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
                    mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
                    jsonResult = mapper.writeValueAsString(result);
                } catch ( Exception e ) {
                    jsonResult = "{}";
                }
                if( jsonResult != null && jsonResult.length() > sgdbfLogging.maxLength() ) {
                    jsonResult = jsonResult.substring(0, sgdbfLogging.maxLength()) + ELLIPSIS;
                }
                MDC.put(RESULT, jsonResult);
            }
            putDuration();

            // log the exit message
            String messageExit = String.format(EXIT_S_S, className, methodName);
            log.info( messageExit);
            MDC.clear();
        }

        private void logError(Exception ex, String cause, String message) {
            MDC.put(EXCEPTION, ex.getMessage());
            putDuration();

            // log exception if an error has occurred
            String messageException = String.format(message, className, methodName, cause);
            log.error( messageException);
            MDC.clear();
        }

        private void putDuration() {
            long duration = Duration.between(before, Instant.now()).toMillis();
            MDC.put(DURATION, String.valueOf(duration));
        }
    }
}
