package org.anyline.simple.derby;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.data.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication 
public class DerbyApplication {
    private static Logger log = LoggerFactory.getLogger(DerbyApplication.class);
    private static AnylineService service      ;
    private static JdbcTemplate jdbc               ;
    private static String catalog  = null          ; // 默认数据库名
    private static String schema   = null          ; // 默认PUBLIC
    private static String table    = "CRM_USER"    ; // 表名

    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(DerbyApplication.class);
        ConfigurableApplicationContext context = application.run(args);

    }

}
