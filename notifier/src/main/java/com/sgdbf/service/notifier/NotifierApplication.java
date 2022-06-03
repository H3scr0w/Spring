package com.sgdbf.service.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The type Notifier application.
 */
@SpringBootApplication
@EnableScheduling
public class NotifierApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifierApplication.class);

    private static ConfigurableApplicationContext context;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws UnknownHostException the unknown host exception
     */
	public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(NotifierApplication.class);
        context = app.run(args);
        Environment env = context.getEnvironment();
        LOGGER.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
	}


    /**
     * The restart entry point of application
     */
    public static void restart()  {
        LOGGER.info("Restarting Application");
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(NotifierApplication.class, args.getSourceArgs());
            Environment env = context.getEnvironment();
            try {
                LOGGER.info("\n----------------------------------------------------------\n\t" +
                                "Application '{}' has restarted and running! Access URLs:\n\t" +
                                "External: \thttp://{}:{}\n----------------------------------------------------------",
                        env.getProperty("spring.application.name"),
                        InetAddress.getLocalHost().getHostAddress(),
                        env.getProperty("server.port"));
            } catch (UnknownHostException e) {
                LOGGER.error("Impossible to restart", e);
            }
        });

        thread.setDaemon(false);
        thread.start();
    }
}
