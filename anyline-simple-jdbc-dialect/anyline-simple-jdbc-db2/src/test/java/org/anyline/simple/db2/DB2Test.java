package org.anyline.simple.db2;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.config.db.SQLAdapter;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

@SpringBootTest
public class DB2Test {
    private Logger log = LoggerFactory.getLogger(DB2Test.class);
    @Autowired
    private AnylineService service          ;
    @Autowired
    private JdbcTemplate jdbc               ;
    private String catalog  = null          ; // DB2不支持
    private String schema   = null          ; // 如 DB2INST1
    private String table    = "CRM_USER"    ; // 表名


    @Test
    public void help() throws Exception{
        ResultSet set = jdbc.getDataSource().getConnection().getMetaData().getTables(null, null, table, "TABLE".split(","));
        ResultSetMetaData md = set.getMetaData();
        while (set.next()) {
            System.out.println("--------------------------------------");
            for (int i = 1; i <= md.getColumnCount(); i++) {
                String column = md.getColumnName(i);
                System.out.print(column + "=");
                Object value = set.getObject(i);
                System.out.println(value);
            }
        }
    }
    @Test
    public void ddl() throws Exception{
        //检测表结构
        Table table = service.metadata().table(catalog, schema, this.table);
        //如果存在则删除
        if(null != table){
            service.ddl().drop(table);
        }
        //也可以直接删除
        service.ddl().drop(new Table(catalog, schema, this.table));

        //再查询一次
        table = service.metadata().table(catalog, schema, this.table);
        Assertions.assertNull(table);

        //定义表结构
        table = new Table(catalog, schema, this.table);

        //添加列
        //自增长列 如果要适配多种数据库 setAutoIncrement 有必须的话可以设置起始值与增量 setAutoIncrement(int seed, int step)
        table.addColumn("ID", "INT", false, null).setComment("主键").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("CODE", "VARCHAR(50)").setComment("编号");
        table.addColumn("NAME", "VARCHAR(50)").setComment("名称");
        //默认当前时间 如果要适配多种数据库 用 SQL_BUILD_IN_VALUE.CURRENT_TIME
        table.addColumn("REG_TIME", "datetime").setComment("名称").setDefaultValue(SQLAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("DATA_VERSION", "double", false, 1.1).setComment("数据版本");

        //创建表
        service.ddl().create(table);

        //再查询一次
        table = service.metadata().table(catalog, schema, this.table);
        Assertions.assertNotNull(table);
    }
    @Test
    public void dml() throws Exception{
        DataSet set = new DataSet();
        for(int i=1; i<=10; i++){
            DataRow row = new DataRow();
            //只插入NAME  ID自动生成 REG_TIME 默认当时时间
            row.put("NAME", "N"+i);
            set.add(row);
        }
        int qty = service.insert(table, set);
        log.warn(LogUtil.format("[insert result][插入数量:{}]", 36), qty);
        Assertions.assertEquals(qty , 10);

        //查询全部数据
        set = service.querys(table);
        log.warn(LogUtil.format("[query result][查询数量:{}]", 36), set.size());
        log.warn("[多行查询数据]:{}",set.toJSON());
        Assertions.assertEquals(set.size() , 10);

        //只查一行
        DataRow row = service.query(table);
        log.warn("[单行查询数据]:{}",row.toJSON());
        Assertions.assertNotNull(row);
        Assertions.assertEquals(row.getId(), "1");

        //查最后一行
        row = service.query(table, "ORDER BY ID DESC");
        log.warn("[单行查询数据]:{}",row.toJSON());
        Assertions.assertNotNull(row);
        Assertions.assertEquals(row.getInt("ID",10), 10);

        //更新
        //put覆盖了Map.put返回Object
        row.put("NAME", "SAVE NAME");

        //set由DataRow声明实现返回DataRow可以链式操作
        row.set("CODE", "SAVE CODE").set("DATA_VERSION", 1.2);

        //save根据是否有主键来判断insert | update
        //可以指定SAVE哪一列
        service.save(row, "NAME");
        service.save(row);
        row.put("NAME", "UPDATE NAME");

        service.update(row,"NAME");

/*
        row.remove("ROW_NUMBER");
        set.remove("ROW_NUMBER");
        set.setPrimaryKey("ID");
        service.delete(set);
        System.out.println(set);

        row = service.query("a_test");
        row.remove("ROW_NUMBER");
        set.remove("ROW_NUMBER");
        set.setPrimaryKey("ID");
        service.delete(set);
        System.out.println(set);*/
    }
}
