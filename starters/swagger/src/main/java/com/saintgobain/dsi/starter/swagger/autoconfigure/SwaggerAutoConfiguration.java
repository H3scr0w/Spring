package com.saintgobain.dsi.starter.swagger.autoconfigure;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.saintgobain.dsi.starter.swagger.properties.SwaggerProperties;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Swagger configuration.
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties({
        SwaggerProperties.class })
public class SwaggerAutoConfiguration {

    /**
     * Api docket for custom config swagger
     *
     * @param swaggerProperties the swagger properties
     * @return the docket
     */
    @Bean
    public Docket swaggerDocket(SwaggerProperties swaggerProperties) {
        // Contact appear in swagger info
        Contact contact = new Contact(
                swaggerProperties.getContactName(),
                swaggerProperties.getContactUrl(),
                swaggerProperties.getContactEmail());

        // Informations appear in swagger info attribute
        ApiInfo apiInfo = new ApiInfo(
                swaggerProperties.getTitle(),
                swaggerProperties.getDescription(),
                swaggerProperties.getVersion(),
                swaggerProperties.getTermsOfServiceUrl(),
                contact,
                swaggerProperties.getLicense(),
                swaggerProperties.getLicenseUrl(),
                new ArrayList<>());

        SecurityScheme apiKey = new ApiKey(swaggerProperties.getAuthorizationName(), swaggerProperties
                .getAuthorizationName(), "header");

        // custom config for endpoints to appear in swagger
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .securitySchemes(Collections.singletonList(apiKey))
                .forCodeGeneration(true)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
                .paths(regex(swaggerProperties.getDefaultIncludePattern()))
                .build();
    }
}
