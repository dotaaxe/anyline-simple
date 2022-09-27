package org.anyline.simple.mysql;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNaviImpl;
import org.anyline.jdbc.param.ConfigStore;
import org.anyline.jdbc.param.init.SimpleConfigStore;
import org.anyline.jdbc.prepare.RunPrepare;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
        test();
    }

    public static void test(){


    }
}
