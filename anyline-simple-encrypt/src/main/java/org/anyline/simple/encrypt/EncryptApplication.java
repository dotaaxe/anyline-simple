package org.anyline.simple.encrypt;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class EncryptApplication {
    private static Logger log = LoggerFactory.getLogger(EncryptApplication.class);
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EncryptApplication.class);
        ConfigurableApplicationContext context = application.run(args);
    }

}
