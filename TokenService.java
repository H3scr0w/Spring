package com.saintgobain.dsi.group.directory.service;

import com.saintgobain.dsi.group.directory.config.SaintGobainProperties;
import com.saintgobain.dsi.group.directory.domain.Person;
import com.saintgobain.dsi.group.directory.domain.UserGroup;
import com.saintgobain.dsi.group.directory.exception.BusinessException;
import com.saintgobain.dsi.group.directory.exception.BusinessJwtException;
import com.saintgobain.dsi.group.directory.exception.TokenException;
import com.saintgobain.dsi.group.directory.security.Jwt;
import com.saintgobain.dsi.group.directory.security.SecurityUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Token service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final SaintGobainProperties properties;
    private final RestTemplate restTemplate;

    /**
     * Gets jwt.
     *
     * @return the jwt
     * @throws BusinessException the business exception
     */
    public Jwt getJwt() throws BusinessException {
        String sgid = getCurrentUser().orElseThrow(() -> new BusinessJwtException("No user found in context"));
        List<String> list = getGroups(sgid);
        return Jwt.builder().jwt(generateJwt(sgid, list)).build();
    }

    private List<String> getGroups(String sgid) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityUtils.SG_HEADER_USERNAME, properties.getRest2ldap().getUsername());
        headers.add(SecurityUtils.SG_HEADER_PD, properties.getRest2ldap().getPassword());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        try {
            String url = this.properties.getRest2ldap().getUsers() + "/" + sgid + "?_fields=groups";
            ResponseEntity<Person> response = restTemplate.exchange(url, HttpMethod.GET, entity, Person.class);
            return response.getBody().getGroups().stream().map(UserGroup::get_id).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting user from API Directory: ", e);
            return Collections.emptyList();
        }
    }

    private String generateJwt(String sgid, List<String> groups) throws TokenException {
        PrivateKey privKey = getPrivateKey();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(privKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Digital-Solutions Delivery");
        claims.setExpirationTimeMinutesInTheFuture(properties.getSecurity().getJwt().getTokenValidityInMinutes());
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject(sgid);
        String compressGroups;
        try {
            compressGroups = compressString(String.join(";", groups));
        } catch (IOException e) {
            throw new TokenException("Error generating jwt", e);
        }
        claims.setStringClaim("groups", compressGroups);
        jws.setPayload(claims.toJson());

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new TokenException("Error generating jwt", e);
        }
    }

    private PrivateKey getPrivateKey() throws TokenException {
        byte[] byteArray = readPrivateKey();
        if (byteArray == null) {
            return null;
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteArray);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TokenException("Error reading jwt private file", e);
        }
    }

    private byte[] readPrivateKey() throws TokenException {
        Resource key = properties.getSecurity().getKey();
        try {
            return IOUtils.toByteArray(key.getInputStream());
        } catch (IOException e) {
            throw new TokenException("Error reading jwt private file", e);
        }
    }

    private Optional<String> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
            .ofNullable(securityContext.getAuthentication())
            .map(
                authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                }
            );
    }

    private String compressString(String srcTxt) throws IOException {
        ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(rstBao);
        zos.write(srcTxt.getBytes());
        IOUtils.closeQuietly(zos);
        byte[] bytes = rstBao.toByteArray();
        return Base64.encodeBase64String(bytes);
    }
}
