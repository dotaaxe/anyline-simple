package org.anyline.simple.tdengine;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class TdengineApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(TdengineApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = context.getBean(AnylineService.class);

        Table table = service.metadata().table("a_test");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("a_test");
        //第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键
        table.addColumn("ID", "TIMESTAMP");
        table.addColumn("CODE","NCHAR(10)");
        table.addColumn("age","int");

        service.ddl().save(table);
        DataSet set = new DataSet();
        for(int i=0; i<10; i++){
            DataRow row = new DataRow();
            row.put("ID", System.currentTimeMillis()+i);
            row.put("CODE","C"+i);
            row.put("age", ""+i*10);
            set.add(row);
        }
        service.insert("a_test", set);
        int total = service.count("a_test");
        PageNavi navi = new PageNaviImpl();
        navi.setTotalRow(total);
        navi.setCurPage(2);
        navi.setPageRows(3);
        set = service.querys("a_test",navi);
        System.out.println(set);

    }
}
