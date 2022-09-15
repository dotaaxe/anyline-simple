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
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = (AnylineService) context.getBean("anyline.service");
        Table table = service.metadata().table("a_test");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("a_test");
        table.addColumn("ID", "int");
        table.addColumn("NAME","varchar2(20)");
        service.ddl().save(table);
        DataSet set = new DataSet();
        for(int i=1; i<=10; i++){
            DataRow row = new DataRow();
            row.put("ID", i);
            row.put("NAME", "N"+i);
            set.add(row);
        }
        service.insert("a_test", set);
        List<User> list = new ArrayList<>();
        for(int i=11; i<=20; i++){
            User user = new User();
            user.setId(i);
            user.setName("NAME-"+i);
            list.add(user);
            //service.insert("a_test", user);
        }
        service.insert("a_test", list, false);


        set = service.querys("a_test");
        DataRow row = service.query("a_test");



        row.remove("ROW_NUMBER");
        set.remove("ROW_NUMBER");
        set.setPrimaryKey("ID");
        service.delete(set);
        System.out.println(set);

    }
}
