package com.saintgobain.dsi.starter.zipkin.autoconfigure;

import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Zipkin auto configuration.
 */
@Configuration
public class ZipkinAutoConfiguration {

    /**
     * Default sampler sampler.
     *
     * @return the sampler
     */
    @Bean
    public Sampler defaultSampler() {
        return new AlwaysSampler();
    }
}
