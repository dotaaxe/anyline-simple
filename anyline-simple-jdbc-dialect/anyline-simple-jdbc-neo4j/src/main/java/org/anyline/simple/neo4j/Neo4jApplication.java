package org.anyline.simple.neo4j;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.service.AnylineService;
import org.anyline.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class Neo4jApplication {
    private static Logger log = LoggerFactory.getLogger(Neo4jApplication.class);
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(Neo4jApplication.class);
        ConfigurableApplicationContext context = application.run(args);


    }
}
