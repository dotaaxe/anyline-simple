package org.anyline.simple.oracle;

import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.data.jdbc.oracle.OracleAdapter;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.*;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.anyline.util.LogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OracleTest {
    private Logger log = LoggerFactory.getLogger(OracleTest.class);
    @Autowired
    private AnylineService service          ;
    @Autowired
    private JdbcTemplate jdbc               ;
    private String catalog  = null          ; // ORACLE不支持
    private String schema   = null          ; // ORACLE中可以相当于数据库名
    private String table    = "CRM_USER"    ; // 表名

    @Test
    public void version() throws SQLException {
        String name = jdbc.getDataSource().getConnection().getMetaData().getDatabaseProductName();
        String version = jdbc.getDataSource().getConnection().getMetaData().getDatabaseProductVersion();
        System.out.println(name);
        System.out.println(version);
    }

    @Test
    public void ddl() throws Exception{
        ConfigTable.IS_THROW_SQL_UPDATE_EXCEPTION = true; //遇到SQL异常直接抛出
        //检测表结构
        Table table = service.metadata().table(catalog, schema, this.table);
        //如果存在则删除
        if(null != table){
            service.ddl().drop(table);
        }
        //也可以直接删除(需要数据库支持 IF EXISTS)
        //service.ddl().drop(new Table(catalog, schema, this.table));

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
        table.addColumn("REG_TIME", "datetime").setComment("注册时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("DATA_VERSION", "DECIMAL(10,2)", false, 1.1).setComment("数据版本");

        //创建表
        service.ddl().create(table);

        //再查询一次
        table = service.metadata().table(catalog, schema, this.table);
        Assertions.assertNotNull(table);

        if(null != service.query("USER_SEQUENCES","SEQUENCE_NAME:SIMPLE_SEQ")) {
            service.execute("DROP SEQUENCE SIMPLE_SEQ");
        }
        String sql = "CREATE SEQUENCE SIMPLE_SEQ MINVALUE 0 START WITH 0 NOMAXVALUE INCREMENT BY 1 NOCYCLE CACHE 100";

        service.execute(sql);

    }
    @Test
    public void dml() throws Exception{
        long qty = 0;
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        DataSet set = null;
        DataRow row = null;

        //序列查询

        BigDecimal next = service.sequence("SIMPLE_SEQ");
        DataRow nexts = service.sequences("SIMPLE_SEQ", "SIMPLE_SEQ2");
        BigDecimal cur = service.sequence(false,"SIMPLE_SEQ");
        DataRow curs = service.sequences(false, "SIMPLE_SEQ", "SIMPLE_SEQ2");

        row = new DataRow();
        row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        row.put("NAME", "N");
        //当前时间，如果要适配多种数据库环境尽量用SQL_BUILD_IN_VALUE,如果数据库明确可以写以根据不同数据库写成: row.put("REG_TIME","${now()}"); sysdate,getdate()等等
        row.put("REG_TIME", JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        qty = service.insert(table, row);
        set = service.querys(table);
        row = new DataRow();
        row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        row.put("NAME", "N");
        row.put("REG_TIME", new java.sql.Timestamp(System.currentTimeMillis()));
        qty = service.insert(table, row);

        row = new DataRow();
        row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        row.put("NAME", "N");
        row.put("REG_TIME", new java.sql.Date(System.currentTimeMillis()));
        qty = service.insert(table, row);

        ConfigTable.IS_AUTO_CHECK_METADATA = false;
        row = new DataRow();
        row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        row.put("NAME", "N");
        row.put("REG_TIME", new java.util.Date());
        qty = service.insert(table, row);
        DataSet tmps = service.querys(table);
        tmps.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        service.insert(table, tmps);

        //日期类型 插入String 值  如果不开启IS_AUTO_CHECK_METADATA会抛出异常
        row = new DataRow();
        row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
        row.put("NAME", "N");
        row.put("REG_TIME", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        qty = service.insert(table, row);

        log.warn(LogUtil.format("[单行插入][影响行数:{}][生成主键:{}]", 36), qty, row.getId());
        Assertions.assertEquals(qty , 1);

        set = new DataSet();
        for(int i=1; i<10; i++){
            row = new DataRow();
            row.put("ID", "${SIMPLE_SEQ.NEXTVAL}");
            row.put("NAME", "N"+i);
            set.add(row);
        }
        qty = service.insert(table, set);
        //默认情况下多行插入不返回序列号
        log.warn(LogUtil.format("[批量插入][影响行数:{}][默认情况下多行插入不返回序列号]", 36), qty);
        Assertions.assertEquals(qty , set.size());

        //如果需要返回序列号,在插入数据前会从数据库中提取序列值
        OracleAdapter.IS_GET_SEQUENCE_VALUE_BEFORE_INSERT = true;
        qty = service.insert(table, set);
        log.warn(LogUtil.format("[批量插入][影响行数:{}][生成主键:{}]", 36), qty, set.getStrings("ID"));
        Assertions.assertEquals(qty , set.size());



        set = service.querys("CRM_USER(ID,NAME)");
        log.warn(LogUtil.format("[批量插入][result:{}]", 36), set.toJSON());
        Assertions.assertEquals(set.size() , 19);



        //查询全部数据
        set = service.querys(table);
        log.warn(LogUtil.format("[query result][查询数量:{}]", 36), set.size());
        log.warn("[多行查询数据]:{}",set.toJSON());
        Assertions.assertEquals(set.size() , 19);

        //只查一行
        row = service.query(table, "ORDER BY ID");
        log.warn("[单行查询数据]:{}",row.toJSON());
        Assertions.assertNotNull(row);

        //查最后一行
        row = service.query(table, "ORDER BY ID DESC");
        log.warn("[单行查询数据]:{}",row.toJSON());
        Assertions.assertNotNull(row);

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

        /*
         * 注意这里的page一般不手工创建，而是通过AnylineController中的condition自动构造
         * service.querys("CRM_USER", condition(true, "ID:id","NAME:%name%", TYPE_CODE:[type]), "AGE:>=age");
         * true:表示分页 或者提供int 表示每页多少行
         * ID:表示数据表中的列
         * id:表示http提交的参数名
         * [type]:表示数组
         * */

        //分页查询
        //每页3行,当前第2页(下标从1开始)
        PageNavi page = new DefaultPageNavi(2, 3);

        //无论是否分页 都返回相同结构的DataSet
        set = service.querys(table, page);
        log.warn(LogUtil.format("[分页查询][共{}行 第{}/{}页]", 36), page.getTotalRow(), page.getCurPage(), page.getTotalPage());
        log.warn(set.toJSON());
        Assertions.assertEquals(page.getTotalPage() , 7);
        Assertions.assertEquals(page.getTotalRow() , 19);

        //模糊查询
        set = service.querys("CRM_USER", "NAME:%N%");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());
        set = service.querys("CRM_USER", "NAME:%N");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());
        set = service.querys("CRM_USER", "NAME:N%");
        log.warn(LogUtil.format("[模糊查询][result:{}]", 36), set.toJSON());

        //其他条件查询
        //in
        List<Integer> in = new ArrayList<>();
        in.add(1);
        in.add(2);
        in.add(3);
        ConfigStore condition = new DefaultConfigStore();
        condition.ands("ID", in);

        //not in
        condition.and(Compare.NOT_IN, "NAME", "N1");
        List<Integer> notin = new ArrayList<>();
        notin.add(10);
        notin.add(20);
        notin.add(30);
        condition.and(Compare.NOT_IN, "ID", notin);

        //between
        List<Integer> between = new ArrayList<>();
        between.add(1);
        between.add(10);
        condition.and(Compare.BETWEEN, "ID", between);

        // >=
        condition.and(Compare.GREAT_EQUAL, "ID", "1");

        //前缀
        condition.and(Compare.LIKE_PREFIX, "NAME", "N");

        set = service.querys("CRM_USER", condition);
        log.warn(LogUtil.format("[后台构建查询条件][result:{}]", 36), set.toJSON());
        //Assertions.assertEquals(set.size() , 2);

        qty = service.count(table);
        log.warn(LogUtil.format("[总数统计][count:{}]", 36), qty);
        Assertions.assertEquals(qty , 19);

        //根据默认主键ID更新
        row.put("CODE",1001);
        //默认情况下 更新过的列 会参与UPDATE
        qty = service.update(row);
        log.warn(LogUtil.format("[根据主键更新内容有变化的化][count:{}]", 36), qty);


        //根据临时主键更新,注意这里更改了主键后ID就成了非主键，但未显式指定更新ID的情况下,ID不参与UPDATE
        row.setPrimaryKey("NAME");
        qty = service.update(row);
        log.warn(LogUtil.format("[根据临时主键更新][count:{}]", 36), qty);

        //显示指定更新列的情况下才会更新主键与默认主键
        qty = service.update(row,"NAME","CODE","ID");
        log.warn(LogUtil.format("[更新指定列][count:{}]", 36), qty);

        //根据条件更新
        ConfigStore store = new DefaultConfigStore();
        store.and(Compare.GREAT, "ID", "1")
                .ands("CODE","1","2","3")
                .and(" CODE > 1")
                .and("NAME IS NOT NULL");
        qty = service.update(row, store);
        log.warn(LogUtil.format("[根据条件更新][count:{}]", 36), qty);


        qty = service.delete(set);
        log.warn("[根据ID删除集合][删除数量:{}]", qty);
        Assertions.assertEquals(qty, set.size());

        //根据主键删除
        qty = service.delete(row);
        log.warn("[根据ID删除][删除数量:{}]", qty);
        Assertions.assertEquals(qty, 1);

        set = service.querys(table, "ID:1");
        qty = service.delete(table, "ID","1");
        log.warn("[根据条件删除][删除数量:{}]", qty);
        Assertions.assertEquals(qty, set.size());

        set = service.querys(table, "ID IN(2,3)");
        qty = service.deletes(table, "ID","2","3");
        log.warn("[根据条件删除][删除数量:{}]", qty);
        Assertions.assertEquals(qty, set.size());
    }

    @Test
    public void help() throws Exception{
        Connection con = jdbc.getDataSource().getConnection();
        System.out.println("\n--------------[metadata]------------------------");
        System.out.println("catalog:"+con.getCatalog());
        System.out.println("schema:"+con.getSchema());
        ResultSet set = con.getMetaData().getTables(null, null, table, "TABLE".split(","));
        ResultSetMetaData md = set.getMetaData();
        if (set.next()) {
            System.out.println("\n--------------[table metadata]------------------------");
            for (int i = 1; i <= md.getColumnCount(); i++) {
                String column = md.getColumnName(i);
                System.out.print(BasicUtil.fillRChar(column, " ",20) + " = ");
                Object value = set.getObject(i);
                System.out.println(value);
            }
        }
        set = jdbc.getDataSource().getConnection().getMetaData().getColumns(null, null, null, null);
        md = set.getMetaData();
        if (set.next()) {
            System.out.println("\n--------------[column metadata]------------------------");
            for (int i = 1; i <= md.getColumnCount(); i++) {
                String column = md.getColumnName(i);
                System.out.print(BasicUtil.fillRChar(column, " ",37) + " = ");
                Object value = set.getObject(i);
                System.out.println(value);
            }
        }

    }
}
