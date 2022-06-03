package com.saintgobain.dsi.starter.security.wsip.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saintgobain.dsi.starter.security.wsip.entity.TokenInfo;
import com.saintgobain.dsi.starter.security.wsip.exception.SaintGobainSecurityException;

public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    private static final String AUTHORIZATION = "Authorization";

    private static final String BEARER = "Bearer ";

    private String jwtPublicKeyPath;

    public SecurityFilter(String jwtPublicKeyPath) {
        this.jwtPublicKeyPath = jwtPublicKeyPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        TokenInfo tokenInfo = null;
        String authorization = request.getHeader(AUTHORIZATION);

        if (StringUtils.isBlank(authorization) || !StringUtils.startsWith(authorization, BEARER) || !ArrayUtils
                .isArrayIndexValid(authorization.split(StringUtils.SPACE), 1)) {
            throw new SaintGobainSecurityException("BAD " + AUTHORIZATION + " HEADER FORMAT");
        }

        String[] tokenArray = authorization.split(StringUtils.SPACE);

        try {
            tokenInfo = decode(tokenArray[1]);
        } catch (SaintGobainSecurityException e) {
            LOGGER.error("Error getting sgid", e);

        }

        if (tokenInfo == null || StringUtils.isBlank(tokenInfo.getEmail())) {
            throw new SaintGobainSecurityException("NO JWT or NO SUB");
        }

        authenticate(request, tokenInfo);
        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, TokenInfo tokenInfo) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (tokenInfo.getAccessRights() != null && !tokenInfo.getAccessRights().isEmpty()) {
            grantedAuthorities.addAll(tokenInfo.getAccessRights().stream()
                    .map(accessRight -> new SimpleGrantedAuthority(accessRight))
                    .collect(Collectors.toList()));
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(tokenInfo
                .getEmail(), null,
                grantedAuthorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public TokenInfo decode(String jwt) throws SaintGobainSecurityException {

        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil();
        PublicKey publicKey;
        try {
            publicKey = rsaKeyUtil.fromPemEncoded(readFile());
        } catch (InvalidKeySpecException | JoseException | FileNotFoundException e) {
            throw new SaintGobainSecurityException("Public key not valid or not found", e);
        }

        // create a JWT consumer
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(publicKey)
                .setSkipDefaultAudienceValidation()
                .build();

        // validate and decode the jwt
        JwtClaims jwtDecoded;
        try {
            jwtDecoded = jwtConsumer.processToClaims(jwt);
        } catch (InvalidJwtException e) {
            throw new SaintGobainSecurityException("Invalid token", e);
        }

        TokenInfo tokenInfo = new TokenInfo();

        try {
            tokenInfo.setEmail(jwtDecoded.getStringClaimValue("sub"));
        } catch (MalformedClaimException e) {
            throw new SaintGobainSecurityException("SGID not found in token", e);
        }

        Set<String> accessRights = new HashSet<>();
        Object compressAccessRights = jwtDecoded.getClaimValue("accessRights");

        if (compressAccessRights instanceof String) {
            // Only one accessRight
            accessRights = Arrays.asList((String) compressAccessRights).stream().collect(Collectors.toSet());

        } else if (compressAccessRights instanceof List) {
            // List of accessRights
            accessRights = (Set<String>) ((List<String>) compressAccessRights).stream().collect(Collectors.toSet());
        } else {
            throw new SaintGobainSecurityException("Invalid Access Rights");
        }

        tokenInfo.setAccessRights(accessRights);
        try {
            tokenInfo.setExpires(jwtDecoded.getExpirationTime().getValue());
        } catch (MalformedClaimException e) {
            throw new SaintGobainSecurityException("Expiration time not found in token", e);
        }
        return tokenInfo;
    }

    private String readFile() throws FileNotFoundException {
        InputStream inputStream = loadFile(jwtPublicKeyPath);
        if (inputStream == null) {
            throw new FileNotFoundException(jwtPublicKeyPath + " not found");
        }
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    private InputStream loadFile(String path) throws FileNotFoundException {
        File f = new File(path);
        if (f.isFile()) {
            return new FileInputStream(f);
        } else {
            return this.getClass().getClassLoader().getResourceAsStream(path);
        }
    }

}
