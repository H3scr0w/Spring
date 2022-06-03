package com.saintgobain.dsi.starter.security.wsip.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class SecurityActuatorfilter extends OncePerRequestFilter {

    private final String username;
    private final String password;

    public SecurityActuatorfilter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }

        String encode = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));

        if (StringUtils.equals(authorization, "Basic " + encode)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("Actuator",
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ACTUATOR")));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
