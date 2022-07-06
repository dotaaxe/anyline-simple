package org.anyline.simple.entity;


import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@SpringBootApplication
public class EntityApplication {
    private static AnylineService service;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EntityApplication.class);
        application.run(args);

        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService)context.getBean("anyline.service");
        run();
    }
    public static void run(){
        DataSet set = service.querys("HR_EMPLOYEE");
        System.out.println(set.toJSON());
        List<Employee> list = service.querys(Employee.class);
        System.out.println(BeanUtil.object2json(list));
    }

}
