package org.anyline.simple.timescale;

import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class TimescaleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TimescaleApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
    }
}
