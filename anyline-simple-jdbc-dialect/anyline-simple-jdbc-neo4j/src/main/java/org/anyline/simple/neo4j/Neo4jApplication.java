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
        service.execute("create (d:Dept{name:'财务部', leader:'张三'})");
        DataSet set = service.querys("match (s:Dept) return s,s.name limit 100 ");
        for(DataRow row:set){
            System.out.println(row);
            DataRow ss = row.getRow("s");
            System.out.println(ss.getName());
        }
    }
}
