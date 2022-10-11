package org.anyline.simple.mssql;


import org.anyline.entity.DataSet;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MSSQLApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MSSQLApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        DataSet set = service.querys("ABM100T", new DefaultPageNavi().setCurPage(2).setPageRows(10));
        System.out.println(set);

    }
}
