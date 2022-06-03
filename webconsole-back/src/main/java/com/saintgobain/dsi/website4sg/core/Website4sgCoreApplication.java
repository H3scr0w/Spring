package com.saintgobain.dsi.website4sg.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.EnableRetry;

import com.saintgobain.dsi.website4sg.core.config.DefaultProfileUtil;
import com.saintgobain.dsi.website4sg.core.config.SaintGobainProperties;

/**
 * The type website4sg-core application.
 */
@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties({
        SaintGobainProperties.class })
@EnableRetry
public class Website4sgCoreApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Website4sgCoreApplication.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws UnknownHostException the unknown host exception
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Website4sgCoreApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        LOGGER.info("\n----------------------------------------------------------\n\t"
                + "Application '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}\n\t"
                + "External: \t{}://{}:{}\n\t"
                + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }
}
