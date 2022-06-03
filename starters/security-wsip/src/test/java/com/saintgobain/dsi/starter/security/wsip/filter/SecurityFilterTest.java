package com.saintgobain.dsi.starter.security.wsip.filter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.starter.security.wsip.entity.TokenInfo;

@RunWith(SpringRunner.class)
public class SecurityFilterTest {

    private SecurityFilter filter;

    private static final String JWT_KEY = "private.der.b64";

    @Test
    public void decodeTestWithClassPath() throws Exception {

        filter = new SecurityFilter("jwt.public.key");
        String jwtEncoded = generateTokenWithOneRight();
        TokenInfo tokenInfo = filter.decode(jwtEncoded);
        Assert.assertNotNull("Good token info with only one right", tokenInfo);

        jwtEncoded = generateTokenWithManyRights();
        tokenInfo = filter.decode(jwtEncoded);
        Assert.assertNotNull("Good token info with many rights", tokenInfo);
    }

    private String generateTokenWithOneRight() throws Exception {

        final JwtClaims claims = new JwtClaims();
        claims.setSubject("test@saint-gobain.com");
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setIssuedAtToNow();
        claims.setAudience("https://www.google.fr");
        claims.setJwtId("ST-2-8rsY-qR9mhFRVL9lAC1mHtAtyocLT066193");
        claims.setClaim("accessRights", "*:*:ADMIN");

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

        // decode them
        byte[] privateKeyBytes = readFile();
        privateKeyBytes = Base64.decodeBase64(privateKeyBytes);

        // get the private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        jws.setKey(privateKey);

        return jws.getCompactSerialization();

    }

    private String generateTokenWithManyRights() throws Exception {
        final JwtClaims claims = new JwtClaims();
        claims.setSubject("test@saint-gobain.com");
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setIssuedAtToNow();
        claims.setAudience("https://www.google.fr");
        claims.setJwtId("ST-2-8rsY-qR9mhFRVL9lAC1mHtAtyocLT066193");

        List<String> accessRights = Arrays.asList("ddc:saintgobain2v2core:LocalIT", "w:isover:Owner");

        claims.setClaim("accessRights", accessRights);

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

        // decode them
        byte[] privateKeyBytes = readFile();
        privateKeyBytes = Base64.decodeBase64(privateKeyBytes);

        // get the private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        jws.setKey(privateKey);

        return jws.getCompactSerialization();

    }

    private byte[] readFile() {
        String key = JWT_KEY;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(key);
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n")).getBytes();
    }

}
