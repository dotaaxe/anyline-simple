package org.anyline.simple.mariadb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MariaDBApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MariaDBApplication.class);
        application.run(args);
    }
}
