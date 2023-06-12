package org.anylie.simple.opengauss;

import org.anyline.adapter.KeyAdapter;
import org.anyline.entity.data.Table;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class OpengaussApplication {
    private static  AnylineService service;
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(OpengaussApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        service = (AnylineService) ctx.getBean("anyline.service");
        DataRow.DEFAULT_KEY_KASE = KeyAdapter.KEY_CASE.SRC;
        DataRow.DEFAULT_PRIMARY_KEY = "id";
        init();
        System.exit(0);
    }
    public static void init() throws Exception{
        Table table = service.metadata().table("tb_user");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("tb_user");
        table.addColumn("id", "int").setPrimaryKey(true).setAutoIncrement(true);
        table.addColumn("name", "varchar(32)").setComment("姓名");
        table.addColumn("remark", "varchar(32)");
        service.ddl().create(table);

        DataRow row = new DataRow();
        row.put("name","张三");
        service.insert("tb_user", row);
        service.query("tb_user");
    }
}
