package com.sgdbf.starter.aop.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * The type Logging aspect properties.
 */
@ConfigurationProperties(LoggingAspectProperties.SPRING_AOP_LOGGING_PREFIX)
public class LoggingAspectProperties {

    /**
     * The constant SPRING_AOP_LOGGING_PREFIX.
     */
    static final String SPRING_AOP_LOGGING_PREFIX = "sgdbf.aop.logging";

    /**
     * The constant ENABLED_PROPERTY.
     */
    static final String ENABLED_PROPERTY = "enabled";

    /**
     * Defines whether the SGDBF logging is enabled or not (default: disabled).
     */
    private boolean enabled = false;

    /**
     * Defines the list of packages to enabled SGDBF logging.
     */
    private List<String> basePackages;

    /**
     * Defines whether the full stack traces are included in log messages (default: enabled).
     */
    private boolean fullStack = true;

    /**
     * Gets base packages.
     *
     * @return the base packages
     */
    public List<String> getBasePackages() {
        return basePackages;
    }

    /**
     * Sets base packages.
     *
     * @param basePackages the base packages
     */
    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    /**
     * Is full stack boolean.
     *
     * @return the boolean
     */
    public boolean isFullStack() {
        return fullStack;
    }

    /**
     * Sets full stack.
     *
     * @param fullStack the full stack
     */
    public void setFullStack(boolean fullStack) {
        this.fullStack = fullStack;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
