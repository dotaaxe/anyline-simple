package org.anyline.simple.oceanbase;


import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class OceanBaseApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OceanBaseApplication.class);
        application.run(args);
    }
}
