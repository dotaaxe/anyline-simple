package org.anyline.data.jdbc.informix;

import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class InformixApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(InformixApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        DataSet set = service.querys("crm_user",1,2);
        System.out.println(set);
    }
}
