package org.anyline.simple.hello;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class HelloApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HelloApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("BS_VALUE(ID,CODE,NM,VAL)");
        System.out.println("查询数量:"+set.size());
        System.out.println(set.toJSON());
        set.put("TITLE", "${CODE}-${NM}");
        System.out.println(set.toJSON());

    }
}
