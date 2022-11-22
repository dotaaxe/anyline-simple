package org.anyline.simple.neo4j;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class Neo4jTest {
    private Logger log = LoggerFactory.getLogger(Neo4jTest.class);
    @Autowired
    private AnylineService service          ;
    @Autowired
    private JdbcTemplate jdbc               ;
    private String catalog  = null          ; //
    private String schema   = null          ; //
    private String table    = "CRM_USER"    ; // 表名


    @Test
    public void dml() throws Exception{
        DataRow.DEFAULT_KEY_KASE = KeyAdapter.KEY_CASE.SRC;
        //以下说到的标签相当于关系型数据库中的表
        //原生CQL创建节点
        service.execute("create (d:Dept{name:'财务部', leader:'张三'})");
        //创建多标签(表)节点
        service.execute("CREATE (e:User:Employee{NAME:'张三',AGE:20})");
        //创建多节点
        service.execute("CREATE (e:User{NAME:'张三',AGE:20})");

        //根据DataRow创建节点
        DataRow r = new DataRow();
        r.put("NAME","ZH");
        r.put("AGE", 20);
        int qty = service.insert("User", r);
        log.warn(LogUtil.format("[DataRow单行插入][影响行数:{}][生成主键:{}]", 36), qty, r.getId());
        Assertions.assertEquals(qty, 1);
        Assertions.assertNotNull(r.getId());

        //创建多标签(表)节点,用:将多个标签分隔
        service.insert("User:Employee", r);
        log.warn(LogUtil.format("[DataRow多标签单行插入][影响行数:{}][生成主键:{}]", 36), qty, r.getId());
        Assertions.assertEquals(qty, 1);
        Assertions.assertNotNull(r.getId());

        //根据entity创建节点
        User u = new User("u1", 10);
        service.insert(u);
        log.warn(LogUtil.format("[Entity单行插入][影响行数:{}][生成主键:{}]", 36), qty, u.getId());
        Assertions.assertEquals(qty, 1);
        Assertions.assertNotNull(u.getId());

        //临时指定标签(表)
        service.insert("set_user",u);
        log.warn(LogUtil.format("[Entity单行插入][临时指定目标表][影响行数:{}][生成主键:{}]", 36), qty, u.getId());
        Assertions.assertEquals(qty, 1);
        Assertions.assertNotNull(u.getId());

        //指定多标签(表)
        service.insert("User:Employee", u);
        log.warn(LogUtil.format("[Entity多标签单行插入][影响行数:{}][生成主键:{}]", 36), qty, u.getId());
        Assertions.assertEquals(qty, 1);
        Assertions.assertNotNull(u.getId());


        //根据DataSet创建节点
        DataSet set = new DataSet();
        for(int i=1; i<=10; i++){
            DataRow row = new DataRow();
            row.put("NAME", "user"+i);
            row.put("AGE", i*5);
            set.add(row);
        }
        qty = service.insert("Batch", set);
        log.warn(LogUtil.format("[DataSet批量插入][影响行数:{}][生成主键:{}]", 36), qty, set.getStrings("ID"));
        Assertions.assertEquals(qty, set.size());
        Assertions.assertEquals(set.getStrings("ID").size(), set.size());

        List<User> users = new ArrayList<>();
        for(int i=1; i<=10; i++){
            users.add(new User("euser"+i, i*3));
        }
        qty = service.insert(users);
        log.warn(LogUtil.format("[List<Entity>批量插入][影响行数:{}]", 36), qty);
        Assertions.assertEquals(qty, users.size());

        qty = service.insert("Batch", (Object) users);
        log.warn(LogUtil.format("[List<Entity>批量插入临时指定表][影响行数:{}][users:{}]", 36), qty, BeanUtil.object2json(users));
        Assertions.assertEquals(qty, users.size());

        //合计
        int total = service.count("Dept","leader:张三");
        log.warn(LogUtil.format("[合计数量][total:{}]", 36), total);

        total = service.count("");
        log.warn(LogUtil.format("[整库合计数量][total:{}]", 36), total);

        //节点是否存在
        boolean exists = service.exists("Dept","leader:张三");
        log.warn(LogUtil.format("[检测节点][exists:{}]", 36), exists);

        //查询
        set = service.querys("match (s:Dept) return s.name, s.qty limit 3 ");
        log.warn(LogUtil.format("[原生CQL查询][result:{}]", 36), set.toJSON());

        //按条件查询,查询结果不含前缀
        set = service.querys("Dept","leader:张三","id:>0");
        log.warn(LogUtil.format("[按条件查询][result:{}]", 36), set.toJSON());


        //模糊查询
        set = service.querys("Dept", "name:%财务%");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());
        set = service.querys("Dept", "name:%财务");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());
        set = service.querys("Dept", "name:财务%");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());


        //按条件分页查询
        PageNavi navi = new DefaultPageNavi(3, 10);
        set = service.querys("Dept", navi, "leader:张三");
        log.warn(LogUtil.format("[按条件分页查询][result:{}]", 36), set.toJSON());

        //指定截取
        set = service.querys("Dept", 0, 9, "leader:张三");
        log.warn(LogUtil.format("[指定截取][result:{}]", 36), set.toJSON());

        set = service.querys("match (s:Dept) return s.name limit 3 ");
        log.warn(LogUtil.format("[原生CQL][result:{}]", 36), set.toJSON());


        set = service.querys("match (s:Dept) return s, s.name as name limit 10 ");

        log.warn(LogUtil.format("[原生CQL][result:{}]", 36), set.toJSON());

        set = service.querys("MATCH (e:Person)-[k:KNOWS]-(f) where e.name='Johan' RETURN e,k, f");
        //返回{e={},k={},f={}}
        log.warn(LogUtil.format("[原生CQL][result:{}]", 36), set.toJSON());

        //按条件删除
        service.delete("Dept", "name","财务部");


        set = service.querys("Batch", 0, 9);
        //根据ID删除
        service.delete("Batch", set);

    }

    @Test
    public void help() throws Exception{
        DataSource ds = null;
        Connection con = null;
        ds = jdbc.getDataSource();
        con = DataSourceUtils.getConnection(ds);
        String query = "MATCH (e:Person)-[k:KNOWS]-(f) where e.name='Johan' RETURN e,labels(e),k, f";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData mt =  rs.getMetaData();
        while(rs.next()) {
            System.out.println("============================================");
            for (int i = 1; i <= mt.getColumnCount(); i++) {
                System.out.println(i + ":" + mt.getColumnName(i));
                Object value = rs.getObject(i);
                System.out.println(value.getClass() +"="+value);

            }
        }
    }
}
