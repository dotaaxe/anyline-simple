package org.anyline.simple.db2;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class DB2Application {
    public static void main(String[] args){
        SpringApplication application = new SpringApplication(DB2Application.class);
        application.run(args);
    }
}
