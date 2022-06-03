package com.saintgobain.dsi.starter.zipkin.postprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Eureka environment post processor.
 */
public class ZipkinEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY = "spring.zipkin.base-url";
    private static final String DEFAULT_NAME = "saint-gobain-spring-boot-zipkin";
    private static final String DEFAULT_VALUE = "${ZIPKIN_URL:http://localhost:9120/}";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication
            springApplication) {

        if (!configurableEnvironment.containsProperty(PROPERTY)) {
            MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            Map<String, Object> map = new HashMap<>();
            map.put(PROPERTY, DEFAULT_VALUE);
            propertySources.addLast(new MapPropertySource(DEFAULT_NAME, map));
        }
    }
}
