package org.anyline.simple.neo4j;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNaviImpl;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class Neo4jApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Neo4jApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = context.getBean(AnylineService.class);
        DataRow.DEFAULT_KEY_KASE = KeyAdapter.KEY_CASE.SRC;

        //当前版本(8.5.7)只支持原生CQL执行，不支持占位符
        //基本上只会用到execute与querys
        //分页请自己查询总数
        service.execute("create (d:Dept{name:'财务部', leader:'张三'})");
        DataSet set = service.querys("match (s:Dept) return s,s.name as name limit 100 ");
        service.querys("Dept",0,10,"name:张三");
        for(DataRow row:set){
            System.out.println(row);
            DataRow ss = row.getRow("s");
            System.out.println(ss.getName());
        }
        DataRow row = new DataRow("User");
        row.put("name","张三");
        row.put("age", 20);
       // service.save(row);
    }
}
