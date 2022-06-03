package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.codec.binary.Base64;

@Converter
public class CryptoPassword implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String KEY = "cQfTjWnZq4t7w!z%";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // Encryption

        if (attribute == null) {
            return null;
        }

        Key key = new SecretKeySpec(KEY.getBytes(), "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String((c.doFinal(attribute.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // Decryption

        if (dbData == null) {
            return null;
        }

        Key key = new SecretKeySpec(KEY.getBytes(), "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.decodeBase64(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
