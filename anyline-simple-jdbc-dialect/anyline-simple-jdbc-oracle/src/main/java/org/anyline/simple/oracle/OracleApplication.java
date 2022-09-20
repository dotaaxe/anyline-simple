package org.anyline.simple.oracle;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class OracleApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(OracleApplication.class);
        application.run(args);

    }
}
