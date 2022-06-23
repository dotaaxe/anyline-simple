package org.anyline.simple.kingbase;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
public class KingBaseApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(KingBaseApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        DataSet set = service.querys("bs_value",1,1);
        System.out.println(set);
    }
}
