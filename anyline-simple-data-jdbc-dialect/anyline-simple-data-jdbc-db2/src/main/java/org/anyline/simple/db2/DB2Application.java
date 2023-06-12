package org.anyline.simple.db2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class DB2Application {
    public static void main(String[] args){
        SpringApplication application = new SpringApplication(DB2Application.class);
        application.run(args);
    }
}
