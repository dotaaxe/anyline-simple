package org.anyline.simple.trans;


import org.anyline.jdbc.ds.DataSourceHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class TransApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(TransApplication.class);
        application.run(args);
    }

}