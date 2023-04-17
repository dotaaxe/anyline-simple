package org.anyline.simple.mysql;


import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MySQLApplication {
    private static JdbcTemplate jdbc;
    private static AnylineService service;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySQLApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        jdbc = context.getBean(JdbcTemplate.class);
        service = context.getBean(AnylineService.class);
        try {
            System.out.println("getCatalog:" + jdbc.getDataSource().getConnection().getCatalog());
            System.out.println("getSchema:" + jdbc.getDataSource().getConnection().getSchema());
            DatabaseMetaData dbmd = jdbc.getDataSource().getConnection().getMetaData();
            ResultSet set =dbmd.getTables("simple", "def", null, null);
            while (set.next()) {
                System.out.println(set.getString("TABLE_NAME"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
