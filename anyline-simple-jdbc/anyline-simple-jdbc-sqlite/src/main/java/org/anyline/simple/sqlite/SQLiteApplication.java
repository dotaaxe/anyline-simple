package org.anyline.simple.sqlite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class SQLiteApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SQLiteApplication.class);
        ConfigurableApplicationContext context = application.run(args);


    }
}
