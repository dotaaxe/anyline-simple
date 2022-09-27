package org.anyline.simple.questdb;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class QuestDBApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(QuestDBApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("SELECT 1");
        System.out.println(set);
        System.exit(0);
    }
}
