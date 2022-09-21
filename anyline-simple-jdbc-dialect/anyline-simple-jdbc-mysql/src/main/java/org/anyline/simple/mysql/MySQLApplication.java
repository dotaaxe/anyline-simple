package org.anyline.simple.mysql;


import org.anyline.entity.DataSet;
import org.anyline.entity.PageNaviImpl;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MySQLApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySQLApplication.class);
        application.run(args);
    }
}
