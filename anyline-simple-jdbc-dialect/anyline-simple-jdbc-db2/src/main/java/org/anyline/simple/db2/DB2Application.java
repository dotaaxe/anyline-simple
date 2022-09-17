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
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(DB2Application.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        Table table = service.metadata().table("a_test");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("a_test");
        table.addColumn("ID", "int");
        table.addColumn("NAME","varchar(20)");
        service.ddl().save(table);
        DataSet set = new DataSet();
        for(int i=1; i<=10; i++){
            DataRow row = new DataRow();
            row.put("ID", i);
            row.put("NAME", "N"+i);
            set.add(row);
        }
        service.insert("a_test", set);

        set = service.querys("a_test");
        DataRow row = service.query("a_test");



        row.remove("ROW_NUMBER");
        set.remove("ROW_NUMBER");
        set.setPrimaryKey("ID");
        service.delete(set);
        System.out.println(set);

    }
}
