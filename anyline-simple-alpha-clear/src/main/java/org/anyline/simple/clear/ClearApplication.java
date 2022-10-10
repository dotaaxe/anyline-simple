package org.anyline.simple.clear;


import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan; 

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class ClearApplication {

    private static AnylineService service;
    private static Logger log = LoggerFactory.getLogger(ClearApplication.class);

    public static void main(String[] args) throws Exception {

        SpringApplication application = new SpringApplication(ClearApplication.class);

        ConfigurableApplicationContext context = application.run(args);

        service = context.getBean(AnylineService.class);


    }
}
