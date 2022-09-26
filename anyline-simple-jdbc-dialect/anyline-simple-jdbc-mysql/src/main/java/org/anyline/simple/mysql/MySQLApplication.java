package org.anyline.simple.mysql;


import org.anyline.entity.DataSet;
import org.anyline.entity.PageNaviImpl;
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
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySQLApplication.class);
        ConfigurableApplicationContext context = application.run(args);

        JdbcTemplate jdbc = context.getBean(JdbcTemplate.class);
        test(jdbc);

    }

    public static void test(JdbcTemplate jdbc){
        List<Object> values = new ArrayList<>();
       // values.add("A1");
       // values.add("A2");
       // values.add("A3");
        String sql = "INSERT INTO CRM_USER(NAME)VALUES('AAA')";
        int cnt = 0;
        Long id = null;
        KeyHolder keyholder = new GeneratedKeyHolder();
            cnt = jdbc.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                    PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    int idx = 0;
                    if (null != values) {
                        for (Object obj : values) {
                            ps.setObject(++idx, obj);
                        }
                    }
                    return ps;
                }
            }, keyholder);
            System.out.println(keyholder.getKeyList());

    }
}
