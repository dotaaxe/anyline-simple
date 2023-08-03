package org.anyline.simple.ds;

import com.zaxxer.hikari.HikariDataSource;
import org.anyline.data.adapter.DriverAdapter;
import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.oracle.OracleAdapter;
import org.anyline.data.jdbc.runtime.JdbcRuntimeHolder;
import org.anyline.data.run.Run;
import org.anyline.data.runtime.DataRuntime;
import org.anyline.entity.DataRow;
import org.anyline.metadata.*;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DDLApplication.class)
public class DDLTest {

    private Logger log = LoggerFactory.getLogger(DDLTest.class);
    @Autowired
    private AnylineService service          ;
    @Autowired
    private JdbcTemplate jdbc               ;


    public void check(String ds, String title) throws Exception{
        System.out.println("\n=============================== START " + title + "=========================================\n");
        if(null != ds) {
            DataSourceHolder.setDataSource(ds);
        }
        check();
        type();
        table();
        view();
        column();
        index();
        exception();
        foreign();
        trigger();
        clear();
        all();
        System.out.println("\n=============================== END " + title + "=========================================\n");
    }

    /**
     * 初始货表
     * @param name 表名
     * @return Table
     */
    public Table init(String name) throws Exception{
        //查询表结构
        Table table = service.metadata().table(name, false); //false表示不加载表结构，只简单查询表名
        //如果已存在 删除重键
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table(name);
        table.addColumn("ID", "bigint").setPrimaryKey(true).setAutoIncrement(true).setComment("主键");
        table.addColumn("CODE", "varchar(20)").setComment("编号");
        table.addColumn("NAME", "varchar(50)").setComment("名称");
        table.addColumn("O_NAME", "varchar(50)").setComment("原列表");
        table.addColumn("SALARY", "decimal(10,2)").setComment("精度").setNullable(false);
        table.addColumn("DEL_COL", "varchar(50)").setComment("删除");
        table.addColumn("CREATE_TIME", "datetime").setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        service.ddl().save(table);
        table = service.metadata().table(name);
        Index index = new Index();
        index.addColumn("SALARY");
        index.addColumn("CODE");
        index.setName("IDX_SALARY_CODE");
        index.setUnique(true);
        index.setTable(table);
        service.ddl().add(index);
        return table;
    }

    /**
     * 修改列类型
     * @throws Exception
     */
    @Test
    public  void all() throws Exception{
        String tableName = "a_test";
        String updateName = "b_test";
        String updateComment = "新comment";
        Table table = init(tableName);
        //修改表名称，修改表注释
        //修改列名称，修改列注释，修改列属性，修改列长度，修改列是否允许为空，修改列主键，修改列默认值，添加列，删除列
        //修索引名称，修改索引注释，修改索引类型，修改索引方法，新增表索引，删除表索引
        /************************************************************************************************************
         *
         *                         rename操作会造成很大的疑惑 请参考 http://doc.anyline.org/aa/e1_3601
         *
         ************************************************************************************************************/

        /*
         * 1.修改表名
         * 修改名称比较特殊，因为需要同时保留新旧名称，否则就不知道要修改哪个表或列了
         * 同时要注意改名不会检测新名称是否存在 所以改名前要确保 新名称 没有被占用
         */
        Table chk = service.metadata().table(updateName, false);
        if(null != chk){
            service.ddl().drop(chk);
        }
        table.update().setName(updateName).setComment(updateComment);
        /*
         * 2.修改属性，注释，非空 如果不存在则创建
         */
        Column col = table.getColumn("CODE");
        if(null == col){
            col = new Column("CODE");
        }
        col.setType("VARCHAR(100)").setDefaultValue("ABC").setComment("新类型").setNullable(false);

        /*
         * 3.删除列
         */
        col = table.getColumn("DEL_COL");
        col.drop();
        /*
         * 4.修改列名
         */
        col = table.getColumn("O_NAME");
        col.update().setName("N_NAME").setComment("新列名");
        /*
         * 5.修改精度
         */
        col = table.getColumn("SALARY");
        col.setPrecision(18);
        col.setScale(9);
        col.setNullable(true);
        /*
         * 6.换主键
         */
        table.setPrimaryKey("CODE");

        service.ddl().save(table);

        table = service.metadata().table(updateName);
        /*
         * 7.删除主键之外的索引
         */
        LinkedHashMap<String,Index> indexs = table.getIndexs();
        for(Index index:indexs.values()){
            if(!index.isPrimary()){
                service.ddl().drop(index);
            }
        }
        /*
         * 8.添加新索引
         */
        Index index = new Index();
        index.addColumn("ID");
        index.addColumn("SALARY");
        index.setName("IDX_ID_SALARY");
        index.setUnique(true);
        index.setTable(table);
        service.ddl().add(index);

        chk = service.metadata().table(tableName);
        //原表名不存在
        Assertions.assertNull(chk);

        //新表名存在
        table = service.metadata().table(updateName);
        Assertions.assertNotNull(table);

        //新表注释
        Assertions.assertEquals(table.getComment(), updateComment);

        //已删除列不存在
        col = table.getColumn("DEL_COL");
        Assertions.assertNull(col);

        //改名列不存在
        col = table.getColumn("O_NAME");
        Assertions.assertNull(col);

        //改名新列存在
        col = table.getColumn("N_NAME");
        Assertions.assertNotNull(col);

        //列注释
        Assertions.assertEquals("新列名", col.getComment());

        //修改类型
        col = table.getColumn("CODE");
        Assertions.assertEquals("VARCHAR(100)", col.getFullType().toUpperCase());

        //默认值
        Assertions.assertEquals("ABC", col.getDefaultValue());

        //null > not null
        Assertions.assertEquals(0, col.isNullable());

        //新主键
        Assertions.assertEquals(1, col.isPrimaryKey());

        //原主键取消
        col = table.getColumn("ID");
        Assertions.assertNotEquals(1, col.isPrimaryKey());

        //原主键自增取消
        Assertions.assertNotEquals(1, col.isAutoIncrement());

        //列长度精度
        col = table.getColumn("SALARY");
        Assertions.assertEquals(18, col.getPrecision());
        Assertions.assertEquals(9, col.getScale());

        //not null > null
        Assertions.assertEquals(1, col.isNullable());

        //原索引删除
        Index idx = table.getIndex("IDX_SALARY_CODE");
        Assertions.assertNull(idx);
        //新索引
        idx = table.getIndex("IDX_ID_SALARY");
        Assertions.assertNotNull(idx);
        //唯一索引
        Assertions.assertTrue(idx.isUnique());




        LinkedHashMap<String,Column> columns = service.metadata().table("b_test").getColumns();
        for(Column column:columns.values()){
            System.out.println("\ncolumn:"+column.getName());
            System.out.println("type:"+column.getFullType());
        }

    }
    //生成SQL不执行
    @Test
    public void sql() throws Exception{
        Table table = new Table("TAB_A");
        table.addColumn("ID", "int").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("CODE", "VARCHAR(10)").setDefaultValue("ABC");
        //如果确定数据库类型直接创建adapter实例
        DriverAdapter adapter = new OracleAdapter();
        List<Run> sqls = adapter.buildCreateRunSQL(table);
        for(Run sql:sqls){
            System.out.println(sql.getFinalUpdate());
        }

        //如果在运行时有多个数据库可以通过SQLAdapterUtil辅助确定数据库类型
        //为什么需要两个参数，getAdapter调用非常频繁,解析过程中需要与数据库建立连接比较耗时，第一个参数是用于缓存，第一次成功解析数据库类型后会缓存起来，后续不再解析
        DataRuntime runtime = JdbcRuntimeHolder.getRuntime();
        adapter = runtime.getAdapter();

        sqls = adapter.buildCreateRunSQL(table);
        for(Run sql:sqls){
            System.out.println(sql.getFinalUpdate());
        }

    }
    public void trigger() throws Exception{
        Table tb = service.metadata().table("TAB_USER", false);
        if(null != tb){
            service.ddl().drop(tb);
        }
        tb = new Table("TAB_USER");
        tb.addColumn("ID","INT").setAutoIncrement(true).setPrimaryKey(true);
        tb.addColumn("CODE", "varchar(10)");
        service.ddl().create(tb);

        Trigger trigger = new Trigger();
        trigger.setName("TR_USER");
        trigger.setTime(org.anyline.metadata.Trigger.TIME.AFTER);
        trigger.addEvent(org.anyline.metadata.Trigger.EVENT.INSERT);
        trigger.setTable("TAB_USER");
        trigger.setDefinition("UPDATE aa SET code = 1 WHERE id = NEW.id;");
        service.ddl().create(trigger);

        trigger = service.metadata().trigger("TR_USER");
        if(null != trigger){
            System.out.println("TRIGGER TABLE:"+trigger.getTableName(true));
            System.out.println("TRIGGER NAME:"+trigger.getName());
            System.out.println("TRIGGER TIME:"+trigger.getTime());
            System.out.println("TRIGGER EVENT:"+trigger.getEvents());
            System.out.println("TRIGGER define:"+trigger.getDefinition());
            service.ddl().drop(trigger);
        }
    }
    public void check() throws Exception{
        for(int i=0; i<100;i++){
            type();
        }
    }
    //外键
    public void foreign() throws Exception{

        Table tb = service.metadata().table("TAB_B");
        if(null != tb){
            service.ddl().drop(tb);
        }
        Table ta = service.metadata().table("TAB_A");
        if(null != ta){
            service.ddl().drop(ta);
        }
        //创建组合主键
        ta = new Table("TAB_A");
        ta.addColumn("ID", "int").setNullable(false).setPrimaryKey(true);
        ta.addColumn("CODE", "varchar(10)").setNullable(false).setPrimaryKey(true);
        ta.addColumn("NAME", "varchar(10)");
        service.ddl().create(ta);


        tb = new Table("TAB_B");
        tb.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true);
        tb.addColumn("AID", "int");
        tb.addColumn("ACODE", "varchar(10)");
        service.ddl().create(tb);
        //创建组合外键
        ForeignKey foreign = new ForeignKey("fkb_id_code");
        foreign.setTable("TAB_B");
        foreign.setReference("TAB_A");
        foreign.addColumn("AID","ID");
        foreign.addColumn("ACODE","CODE");
        service.ddl().add(foreign);

        //查询组合外键
        LinkedHashMap<String, ForeignKey> foreigns = service.metadata().foreigns("TAB_B");
        for(ForeignKey item:foreigns.values()){
            System.out.println("外键:"+item.getName());
            System.out.println("表:"+item.getTableName(true));
            System.out.println("依赖表:"+item.getReference().getName());
            LinkedHashMap<String, Column> columns = item.getColumns();
            for(Column column:columns.values()){
                System.out.println("列:"+column.getName()+"("+column.getReference()+")");
            }
        }
        //根据列查询外键
        foreign = service.metadata().foreign("TAB_B", "AID","ACODE");
        if(null != foreign) {
            System.out.println("外键:" + foreign.getName());
            System.out.println("表:" + foreign.getTableName(true));
            System.out.println("依赖表:" + foreign.getReference().getName());
            LinkedHashMap<String, Column> columns = foreign.getColumns();
            for (Column column : columns.values()) {
                System.out.println("列:" + column.getName() + "(" + column.getReference() + ")");
            }
        }
    }
    public void view(){
        View view = new View("v");
    }
    public void type() throws Exception{
        Table table = service.metadata().table("a_test");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("a_test");
        table.setComment("表备注");
        table.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("REG_TIME", "TIME");
        service.ddl().save(table);
        DataRow row = new DataRow();
        row.put("REG_TIME", LocalTime.now());
        service.insert("a_test", row);
        HikariDataSource ds = (HikariDataSource )DataSourceHolder.getDataSource();
        System.out.println("================活动(未释放)："+ds.getHikariPoolMXBean().getActiveConnections());
        System.out.println("================空闲(可用)："+ds.getHikariPoolMXBean().getIdleConnections());

    }
    public void table() throws Exception{
        System.out.println("\n-------------------------------- start table  --------------------------------------------\n");


        LinkedHashMap<String,Table> tables = service.metadata().tables();
        log.warn("检索表数量:"+tables.size());
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }
        //修改表名

        Table table = service.metadata().table("a_test");
        if(null != table){
            log.warn("删除表:"+table.getName());
            service.ddl().drop(table);
        }
        log.warn("创建表");
        table = new Table();
        table.setName("a_test");
        table.setComment("表备注");
        table.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true).setComment("主键说明");

        table.addColumn("NAME","varchar(50)").setComment("名称");
        table.addColumn("A_CHAR","varchar(50)");
        service.ddl().save(table);
        ConfigTable.IS_DDL_AUTO_DROP_COLUMN = true;
        table = service.metadata().table("a_test");
        table.setName("a_test");
        table.setComment("新备注");
        table.addColumn("a_char","varchar(50)");
        table.addColumn("NAME_"+ BasicUtil.getRandomNumberString(5),"varchar(50)").setComment("添加新列");
        service.ddl().save(table);


        table = service.metadata().table("a_test");
        if(null != table) {
            log.warn("查询表结构:" + table.getName());
            LinkedHashMap<String, Column> columns = table.getColumns();
            for (Column column : columns.values()) {
                log.warn("列:" + column.toString());
            }
        }

        //修改表结构两类场景
        //1.在原表结构上添加修改列
        if(null != table) {
            table.getColumn("NAME").setComment("新备注名称");
            table.addColumn("NAMES", "varchar(50)").setComment("名称S");
            service.ddl().save(table);
        }

        //2.构造表结构(如根据解析实体类的结果) 与数据库对比
        /* ***************************************************************************************************************
         * 		这里务必注意:因为update是新构造的(而不像1中是从数据库中查出来的会包含数据库中所有的列)所以会存在update中不存在而数据库中存在的列
         * 		对于这部分列有两种处理方式:1)从数据库中删除 2)忽略
         * 		默认情况下会忽略,如果要删除可以设置table.setAutoDropColumn(true)
         *
         * ****************************************************************************************************************/

        //如果数据库中已存在同名的表 则执行更新
        Table update = new Table("a_test");//尽量不要new Table  因为表结构上有许多许多的配置项
        update.setAutoDropColumn(true); //删除update中不存在的列
        update.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true).setComment("主键说明");
        update.addColumn("CODE","varchar(50)").setComment("编号");
        //这里表原来少了NAME  NAMES  A_CHAR 三列、执行save时会删除这三列
        service.ddl().save(update);
        table = service.metadata().table("TEST_PK");
        if(null != table){
            service.ddl().drop(table);
        }

        table = new Table("TEST_PK");
        table.addColumn("NAME", "varchar(10)");
        service.ddl().save(table);

        table.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true);
        service.ddl().save(table);


        table = service.metadata().table("TEST_PK");
        Column pcol = table.addColumn("PKID", "int");
        PrimaryKey pk = new PrimaryKey();
        pk.addColumn(pcol);
        //修改主键
        table.setPrimaryKey(pk);
        service.ddl().save(table);
        //修改表名
        table.update().setName("test_pk_"+ DateUtil.format("yyyyMMddHHmmss"));
        service.ddl().save(table);

        System.out.println("\n-------------------------------- end table  ----------------------------------------------\n");
    }
    @Test
    public void column() throws Exception{
        System.out.println("\n-------------------------------- start column  -------------------------------------------\n");

        Table table = service.metadata().table("a_test");
        if(null != table){
            service.ddl().drop(table);
        }

        table = new Table();
        table.setName("a_test");
        table.setComment("表备注");
        table.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true).setComment("主键说明");

        table.addColumn("NAME","varchar(50)").setComment("名称");
        table.addColumn("A_CHAR","varchar(50)");
        table.addColumn("DEL_COL","varchar(50)");
        service.ddl().save(table);

        table = service.metadata().table("a_test");

        //添加列
        String tmp = "NEW_"+BasicUtil.getRandomNumberString(3);
        table.addColumn(tmp, "int");
        service.ddl().save(table);
        //删除列
        Column dcol = table.getColumn(tmp);
        dcol.delete();
        service.ddl().save(table);

        //修改列属性
        Column column = table.getColumn("NAME");
        column.setTypeName("int");	//没有数据的情况下修改数据类型
        column.setPrecision(0);
        column.setScale(0);
        column.setDefaultValue("1");
        column.setNullable(false);
        service.save(column);

        column = new Column();
        column.setTable("a_test");
        column.setName("A_CHAR");
        column.setTypeName("int");	//没有数据的情况下修改数据类型
        column.setPrecision(0);
        column.setScale(0);
        column.setDefaultValue("1");
        column.setNullable(false);

        service.ddl().save(column);

        //修改列
        //没有值的属性 默认同步原有的数据库结构
        //如果不修改列名，直接修改column属性
        column.setTypeName("varchar(10)");
        column.setComment("测试备注1"+ DateUtil.format());
        column.setDefaultValue("2");
        log.warn("修改列");
        service.ddl().save(column);

        //修改列名2种方式
        //注意:修改列名时，不要直接设置name属性,修改数据类型时，不要直接设置typeName属性,因为需要原属性
        // 1.可以设置newName属性(注意setNewName返回的是update)
        column.setNewName("B_TEST").setTypeName("varchar(20)");
        log.warn("修改列名");
        service.ddl().save(column);



        // 2.可以在update基础上修改
        //如果设置了update, 后续所有更新应该在update上执行
        column.update().setName("C_TEST").setPosition(0).setTypeName("VARCHAR(20)");
        log.warn("修改列名");
        service.ddl().save(column);

        column = new Column();
        column.setName("c_test").setNewName("d_test");
        column.setTypeName("varchar(1)");
        column.setTable("a_test");
        service.ddl().save(column);
/*
		column = new Column("id");
		column.setTable("t");
		column.setCatalog("c");
		column.setSchema("s");

		log.warn("删除列");
		service.ddl().drop(column);*/

        Table tab = new Table("c_test");
        tab.addColumn("ID", "int");
        service.ddl().save(tab);
        //添加修改自增(没有实现)

        System.out.println("\n-------------------------------- end column  ---------------------------------------------\n");

    }
    public void exception() throws Exception{

        String tab = "A_EXCEPTION";
        String col = "A_CHAR2INT";
        System.out.println("\n-------------------------------- start exception  ----------------------------------------\n");
        ConfigTable.AFTER_ALTER_COLUMN_EXCEPTION_ACTION				= 1000			;   // DDL修改列异常后 0:中断修改 1:删除列 n:总行数小于多少时更新值否则触发另一个监听
        // 0:中断执行
        // 1:直接修正
        // n:行数<n时执行修正  >n时触发另一个监听(默认返回false)
        Table table = service.metadata().table(tab);
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table(tab);
        table.addColumn("ID", "INT").setPrimaryKey(true).setAutoIncrement(true);
        table.addColumn(col, "VARCHAR(20)");
        service.ddl().create(table);

        table.addColumn("CODE", "INT");
        service.ddl().save(table);
        table = service.metadata().table(tab);
        table.addColumn("CODE", "INT");
        service.ddl().save(table);
        //表中有数据的情况下
        DataRow row = new DataRow();
        //自增列有可能引起异常
        try {
            row.put(col, "123A");
            service.save(tab, row);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        log.warn("表中有数据的情况下修改列数据类型");
        Column column = new Column();
        column.setTable(tab);
        column.setName(col);
        //这一列原来是String类型 现在改成int类型
        //如果值不能成功实现殷式转换(如123A转换成int)会触发一次默认的DDListener.afterAlterException
        //如果afterAlterException返回true，会再执行一次alter column如果还失败就会抛出异常
        //如果不用默认listener可以column.setListener
        column.setTypeName("int");
        column.setPrecision(0);
        service.ddl().save(column);
        System.out.println("\n-------------------------------- end  exception  -----------------------------------------\n");
    }
    public void index() throws Exception{
        System.out.println("\n-------------------------------- start index  --------------------------------------------\n");

        Table tab = new Table("B_TEST");
        if(null != tab){
            service.ddl().drop(tab);
        }
        tab = new Table("B_TEST");
        tab.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
        tab.addColumn("CODE", "INT");
        service.ddl().save(tab);

        LinkedHashMap<String, Index> indexs = service.metadata().indexs("crm_user");
        for(Index item:indexs.values()){
            System.out.println("所引:"+item.getName());
            System.out.println("是否主键:"+item.isPrimary());
            System.out.println("是否物理所引:"+item.isCluster());
            System.out.println("是否唯一:"+item.isUnique());
            LinkedHashMap<String, Column> columns = item.getColumns();
            for(Column column:columns.values()){
                System.out.println("包含列:"+column.getName());
            }
            //如果删除自增长主键 有可能会抛出异常： there can be only one auto column and it must be defined as a key

            System.out.println("删除索引:" + item.getName());
            try {
                if(!item.isPrimary()) {
                    service.ddl().drop(item);
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }

        Index index = service.metadata().index("crm_user_index_CODE");
        if(null == index){
            index = new Index();

            index.setName("crm_user_index_CODE"); //如果不指定名称，会生成一个随机名称，如果指定了名称但与现有索引重名 会抛出异常
            index.setUnique(true);
            index.setTable(tab);
            index.addColumn(new Column("CODE"));
            service.ddl().add(index);
        }

        indexs = service.metadata().indexs("a_test");
        for(Index idx: indexs.values()){
            System.out.println("\n剩余索引:"+idx.getName());
            Map<String,Column> columns = idx.getColumns();
            for(String col:columns.keySet()){
                System.out.println("column:"+col);
            }
        }

        System.out.println("\n-------------------------------- end index  ----------------------------------------------\n");
    }

    public void clear(){
        System.out.println("\n=============================== START clear =========================================\n");
        try {
            service.ddl().drop(new Table("a_test"));
            service.ddl().drop(new Table("b_test"));
            service.ddl().drop(new Table("c_test"));
            service.ddl().drop(new Table("i_test"));
        }catch (Exception e){}
        System.out.println("\n=============================== START clear =========================================\n");
    }



}
