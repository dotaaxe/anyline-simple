package org.anyline.simple.noweb;

import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
@EnableScheduling
public class SimpleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SimpleApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("BS_VALUE");
        System.out.println(set.toJSON());
    }
}
