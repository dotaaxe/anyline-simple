package org.anyline.simple.noweb;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@SpringBootApplication
@EnableScheduling
@Import(DynamicDataSourceRegister.class)
public class SimpleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SimpleApplication.class);
        application.run(args);
    }
}
