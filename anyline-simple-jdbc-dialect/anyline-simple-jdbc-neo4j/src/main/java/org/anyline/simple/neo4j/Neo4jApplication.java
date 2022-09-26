package org.anyline.simple.neo4j;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class Neo4jApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(Neo4jApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = context.getBean(AnylineService.class);
        DataRow.DEFAULT_KEY_KASE = KeyAdapter.KEY_CASE.SRC;
        JdbcTemplate jdbc = context.getBean(JdbcTemplate.class);

        //当前版本(8.5.7)只支持原生CQL执行，不支持占位符
        //基本上只会用到execute与querys
        //分页请自己查询总数

        //以下说到的标签相当于关系型数据库中的表
        //原生CQL创建节点
/*        service.execute("create (d:Dept{name:'财务部', leader:'张三'})");
        //创建多标签(表)节点
        service.execute("CREATE (e:User:Employee{NAME:'张三',AGE:20})");
        //创建多节点
        service.execute("CREATE (e:User{NAME:'张三',AGE:20})");*/

        //根据DataRow创建节点
        DataRow r = new DataRow();
        r.put("NAME","ZH");
        r.put("AGE", 20);
        service.insert("user", r);
        //创建多标签(表)节点
   /*     service.insert("User:Employee", r);

        //根据entity创建节点
        User u = new User("u1", 10);
        service.insert(u);
        //临时指定标签(表)
        service.insert("set_user",u);
        //指定多标签(表)
        service.insert("User:Employee", r);


        //根据DataSet创建节点
        DataSet set = new DataSet();
        for(int i=1; i<=10; i++){
            DataRow row = new DataRow();
            row.put("NAME", "user"+i);
            row.put("AGE", i*5);
            set.add(row);
        }
        service.insert("Batch", set);
        List<User> users = new ArrayList<>();
        for(int i=1; i<=10; i++){
            users.add(new User("euser"+i, i*3));
        }
        service.insert(users);
        service.insert("Batch", (Object) users);


        //合计
        int total = service.count("Dept","leader:张三");
        System.out.println("total:"+total);
        //节点是否存在
        boolean exists = service.exists("Dept","leader:张三");
        System.out.println("exists:"+exists);


        //查询
        set = service.querys("match (s:Dept) return s.name, s.qty limit 3 ");
        for(DataRow row:set){
            System.out.println(row);
        }
        //按条件查询
        set = service.querys("Dept","leader:张三");
        for(DataRow row:set){
            System.out.println(row);
        }

        //按条件分页查询
        PageNavi navi = new PageNaviImpl(3, 10);
        set = service.querys("Dept", navi, "leader:张三");
        for(DataRow row:set){
            System.out.println(row);
        }
        set = service.querys("Dept", 0, 9, "leader:张三");
        for(DataRow row:set){
            System.out.println(row);
        }

        set = service.querys("match (s:Dept) return s.name limit 3 ");
        for(DataRow row:set){
            System.out.println(row);
        }*/

/*
        DataSet set = service.querys("match (s:Dept) return s,s.name as name limit 100 ");
        set = service.querys("MATCH (e:Person)-[k:KNOWS]-(f) where e.name='Johan' RETURN e,k, f");
        //返回{e={},k={},f={}}

        //service.querys("Person",0,10,"name:Johan");
        for(DataRow row:set){
            System.out.println(row);
        }
        DataRow row = new DataRow("User");
        row.put("name","张三");
        row.put("age", 20);*/
       // service.save(row);
/*
        Statement stmt = jdbc.getDataSource().getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("create (n0:Person { name: 'S' }) return id(n0) as id");
        System.out.println(rs);
        while(rs.next())
        {
            System.out.println(rs.getString("id"));
        }*/
    }
}
