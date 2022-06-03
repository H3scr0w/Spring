package com.saintgobain.dsi.starter.security.wsip.autoconfigure;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.saintgobain.dsi.starter.security.wsip.filter.SecurityActuatorfilter;
import com.saintgobain.dsi.starter.security.wsip.filter.SecurityFilter;
import com.saintgobain.dsi.starter.security.wsip.web.Http401UnauthorizedEntryPoint;
import com.saintgobain.dsi.starter.security.wsip.web.Http403AccessDeniedHandler;

/**
 * The type Security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(SaintGobainSecurityProperties.class)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final SaintGobainSecurityProperties properties;

    @Value("${spring.boot.admin.client.instance.metadata.user.name:#{null}}")
    private Optional<String> username;
    @Value("${spring.boot.admin.client.instance.metadata.user.password:#{null}}")
    private Optional<String> password;

    /**
     * Instantiates a new Security auto configuration.
     *
     * @param properties the properties
     */
    public SecurityAutoConfiguration(SaintGobainSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401UnauthorizedEntryPoint())
                .accessDeniedHandler(new Http403AccessDeniedHandler())
                .and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint().excluding(MappingsEndpoint.class)).hasRole("ACTUATOR")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers(HttpMethod.GET, "/", // Home page
                        "/v2/api-docs", // swagger
                        "/webjars/**", // swagger-ui webjars
                        "/swagger-resources/**", // swagger-ui resources
                        "/configuration/**", // swagger configuration
                        "/*.html", // Static file
                        "/favicon.ico", // favicon
                        "/**/*.html", // Sub static file
                        "/**/*.css", // CSS files
                        "/**/*.js" // JS Files
                ).permitAll()
                .antMatchers("/console/**").permitAll();

        if (properties.getExposure() != null && properties.getExposure().getExclude() != null
                && !properties.getExposure().getExclude().isEmpty()) {
            for (String exclude : properties.getExposure().getExclude()) {
                http.authorizeRequests().antMatchers(exclude).permitAll();
            }
        }

        http.authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(new SecurityFilter(properties.getJwtPublicKey()),
                UsernamePasswordAuthenticationFilter.class);
        if (username.isPresent() && password.isPresent()) {
            http.addFilterBefore(new SecurityActuatorfilter(username.get(), password.get()), SecurityFilter.class);
        }

        // disable page caching
        http.headers().cacheControl().and().frameOptions().sameOrigin();
    }
}
