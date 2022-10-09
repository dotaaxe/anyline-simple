package org.anyline.simple.kingbase;

import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class KingBaseApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(KingBaseApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        DataSet set = service.querys("bs_dict",1,1);
        System.out.println(set);
    }
}
