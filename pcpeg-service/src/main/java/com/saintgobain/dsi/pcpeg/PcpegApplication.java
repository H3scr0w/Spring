package com.saintgobain.dsi.pcpeg;

import com.saintgobain.dsi.pcpeg.config.DefaultProfileUtil;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

/**
 * The type pcpeg application.
 */
@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties({ PcpegProperties.class })
@Slf4j
public class PcpegApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws UnknownHostException the unknown host exception
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(PcpegApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}\n\t" +
            "External: \t{}://{}:{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            env.getProperty("server.port"),
            protocol,
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getActiveProfiles()
        );
    }
}
