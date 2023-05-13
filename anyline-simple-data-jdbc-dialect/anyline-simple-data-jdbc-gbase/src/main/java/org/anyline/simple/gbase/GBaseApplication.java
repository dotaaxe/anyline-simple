package org.anyline.simple.gbase;

import org.anyline.data.entity.Column;
import org.anyline.data.entity.Table;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class GBaseApplication {
    private static AnylineService service;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GBaseApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService) context.getBean("anyline.service");
        init();
    }
    public static void init() {
        try {
            Table table = service.metadata().table("crm_user");
            if (null != table) {
                service.ddl().drop(table);
            }
            table = new Table("crm_user");
            Column column = new Column("ID").setAutoIncrement(true).setType("int").setPrimaryKey(true);
            table.addColumn(column);
            table.addColumn("CODE", "varchar(10)");
            table.addColumn("NAME", "varchar(10)");
            service.ddl().create(table);
        }catch (Exception e){
            e.printStackTrace();
        }
        DataRow row = new DataRow();
        row.put("ID", 1);
        row.put("NAME", "张三");
        service.insert("crm_user", row);
        DataSet set = service.querys("crm_user",1,1);
    }
}
