package com.sgdbf.service.notifier.https.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfiguration {

    @Value("${server.https.ssl.key-alias}")
    private String keyAlias;

    @Value("${server.https.ssl.key-store}")
    private String keyStoreFile;

    @Value("${server.https.ssl.key-store-password}")
    private String keyPassword;

    @Value("${server.https.ssl.key-store-type}")
    private String keyStoreType;

    @Value("${server.https.ssl.port}")
    private int sslPort;

    @Value("${server.https.ssl.max-https-header-size}")
    private int maxHeaderSize;

    @Value("${server.https.ssl.max-connections}")
    private int maxConnections;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        connector.setScheme("https");
        connector.setSecure(true);
        protocol.setSSLEnabled(true);

        connector.setPort(sslPort);
        protocol.setKeystoreFile(keyStoreFile);
        protocol.setKeystorePass(keyPassword);
        protocol.setKeyAlias(keyAlias);
        protocol.setKeystoreType(keyStoreType);
        protocol.setMaxHttpHeaderSize(maxHeaderSize);
        protocol.setMaxConnections(maxConnections);
        return connector;
    }
}

