package org.anyline.simple.derby;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class DerbyApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DerbyApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        org.slf4j.impl.StaticLoggerBinder s;
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        String sql = "${CREATE TABLE hr_user(ID INT, NAME varchar(10))}";
        //service.execute(sql);
        DataSet users = new DataSet();
        for(int i=0; i<20;i++) {
            DataRow user = new DataRow();
            user.put("ID", (i+1));
            user.put("NAME", "USER_"+(i+1));
            users.add(user);
        }
        service.insert("hr_user", users);
        DataSet set = service.querys("hr_user",0,2);
        System.out.println(set);
    }
}
