package org.anyline.simple.interceptor;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class InterceptorApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication application = new SpringApplication(InterceptorApplication.class);
        ConfigurableApplicationContext context = application.run(args);

        AnylineService service = context.getBean(AnylineService.class);

        Table table = service.metadata().table("DDL_TABLE");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("DDL_TABLE");
        table.addColumn("ID", "INT");
        table.addColumn("NAME", "VARCHAR(10)");
        service.ddl().create(table);

        Column col = table.getColumn("NAME");
        col.setNewName("NM");
        service.ddl().alter(col);

        service.ddl().rename(col, "RE_NAME");

        DataRow row = new DataRow();
        row.put("RE_NAME","TEST");
        service.insert("DDL_TABLE", row);
        DataSet set = service.querys("DDL_TABLE");
        System.out.println(set);
    }
}
