package org.anyline.simple.validate;

import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.entity.geometry.LineString;
import org.anyline.entity.geometry.Point;
import org.anyline.metadata.*;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
public class ValidateTest {

    private static Logger log = LoggerFactory.getLogger(ValidateTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void check() throws Exception {
        ConfigTable.IS_PRINT_EXCEPTION_STACK_TRACE = true;

         //check(null, "MySQL");
         //check("cms", "MySQL");
        //check("pg", "PostgreSQL");
       //check("ms", "SQL Server");
        // check("ms2000", "SQL Server 2000");
         check("oracle", "Oracle 11G");
        // check("dm8", "达梦8");
        // check("db2", "DB2");
        // check("kingbase8", "人大金仓8(Oracle兼容)");
        // check("gbase", "南大通用");
        // check("opengauss", "高斯");
        // check("oscar", "神州通用");
        // check("informix", "Informix");

		/*for(String datasource:DataSourceHolder.list()){
			check(datasource,datasource);
		}*/


    }
    public void check(String ds, String type) throws Exception {
        System.out.println("======================== start validate " + type + " ================================");
        if(null == ds){
            DataSourceHolder.setDefaultDataSource();
        }else {
            DataSourceHolder.setDataSource(ds);
        }

       //ddl();
        //dml();
        //foreign();

        meta();
       // geometry();
       // text();

      //  generatedKeyHolder();
      //  primary();
        all();
        System.out.println("======================== finish validate " + type + " ================================");
    }
    public void text() throws Exception{

        Table tab = service.metadata().table("TAB_TXT");
        if(null != tab){
            service.ddl().drop(tab);
        }
        tab = new Table("TAB_TXT");
        tab.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
        tab.addColumn("CODE", "INT");
        tab.addColumn("REMARK", "TEXT");
        tab.addColumn("CREATETIME", "datetime");
        service.ddl().save(tab);
        DataRow row = new DataRow();
        if("oracle".equalsIgnoreCase(DataSourceHolder.curDataSource())) {
            row.put("ID", 1);
        }
        row.put("CODE",1);
        row.put("REMARK", "中文ABC123!@^%$#");
        service.insert("TAB_TXT", row);
        DataSet set = service.querys("TAB_TXT", new DefaultConfigStore().setPageNavi(new DefaultPageNavi(0)),"ORDER BY createtime DESC");
        System.out.println(set);
    }
    @Test
    public void generatedKeyHolder() throws Exception{
        Table tab = service.metadata().table("TAB_ID");
        if(null != tab){
            service.ddl().drop(tab);
        }
        tab = new Table("TAB_ID");
        tab.addColumn("CODE", "int");
        service.save(tab);

        DataRow row = new DataRow();
        row.setPrimaryKey(new ArrayList<>());
        row.put("CODE",1);
        service.insert("TAB_ID", row);
        System.out.println(row);

    }
    //主键
    public void primary() throws Exception{
        Table tab = service.metadata().table("TAB_PK");
        if(null != tab){
            service.ddl().drop(tab);
        }
        //创建一个主键的表
        tab = new Table("TAB_PK");
        tab.addColumn("ID", "int").setPrimaryKey(true);
        tab.addColumn("NAME", "varchar(10)");
        service.save(tab);
        tab = service.metadata().table("TAB_PK");
        tab.addColumn("CODE", "INT");
        tab.getColumn("ID").setPrimaryKey(false).setNullable(true);
        //修改成组合主键
        PrimaryKey pk = new PrimaryKey();
        pk.addColumn("NAME").addColumn("CODE"); //其中一列CODE是新加的列
        tab.setPrimaryKey(pk);

        // 因为CODE原来不存在 所以先添加一列CODE
        // 再删除原主键  ALTER TABLE simple.public.tab_a DROP CONSTRAINT pk_tab_a
        // 最后创建新主键    ALTER TABLE simple.public.tab_a ADD PRIMARY KEY (ID,NAME,CODE)
        service.ddl().alter(tab);
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
        ta.addColumn("ID", "int").setPrimaryKey(true);
        ta.addColumn("CODE", "varchar(10)").setPrimaryKey(true);
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
            LinkedHashMap<String,Column> columns = item.getColumns();
            for(Column column:columns.values()){
                System.out.println("列:"+column.getName()+"("+column.getReference()+")");
            }
        }
        //根据列查询外键
        foreign = service.metadata().foreign("TAB_B", "AID","ACODE");
        System.out.println("外键:"+foreign.getName());
        System.out.println("表:"+foreign.getTableName(true));
        System.out.println("依赖表:"+foreign.getReference().getName());
        LinkedHashMap<String,Column> columns = foreign.getColumns();
        for(Column column:columns.values()){
            System.out.println("列:"+column.getName()+"("+column.getReference()+")");
        }

        //删除复合主外键
        foreign = new ForeignKey();
        foreign.setTable("TAB_B");
        foreign.addColumn("AID", null);
        foreign.addColumn("ACODE", null);
        service.ddl().drop(foreign);
    }
    public void geometry(){
        try {
            ConfigTable.IS_AUTO_CHECK_METADATA = true;
            Table table = service.metadata().table("bs_geometry");
            if (null != table) {
                service.ddl().drop(table);
            }
            table = new Table("bs_geometry");
            table.addColumn("ID", "BIGINT").setAutoIncrement(true).setPrimaryKey(true);
            table.addColumn("WORK_LOCATION", "POINT");
            table.addColumn("WORK_TRACE", "LINE");
            service.ddl().create(table);
           // point();
            line();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void point(){
        DataRow row = new DataRow();
        row.put("WORK_LOCATION", new Point(120,36));
        service.insert("bs_geometry", row);
        row = service.query("bs_geometry");
        System.out.println(row);
    }
    public void line(){
        DataRow row = service.query("bs_geometry");
        System.out.println(row);
        row = new DataRow();
        LineString line = new LineString();
        line.add(new Point(120,36)).add(new Point(121,37));
        row.put("WORK_TRACE", line);
        service.insert("bs_geometry", row);
        row = service.query("bs_geometry");
        System.out.println(row);
    }
    public void meta() throws Exception{
        createTable();
        Map<String,Table> tables = service.metadata().tables();
        for(String name:tables.keySet()){
            Table table = tables.get(name);
            System.out.println("table:"+table.getName());
        }
        Table table = service.metadata().table("HR_EMPLOYEE");
        Map<String, Column> columns = table.getColumns();
        for(String name:columns.keySet()){
            Column column = columns.get(name);
            System.out.println("column:"+column.getName() + "\ntype:" + column.getTypeName() + " length:" + column.getPrecision() + " scale:"+column.getScale());
        }
        PrimaryKey pk = table.getPrimaryKey();
        if(null != pk){
            System.out.println(table.getName()+"主键:"+pk.getName());
            Map<String,Column> cols = pk.getColumns();
            for(String name:cols.keySet()){
                Column column = cols.get(name);
                System.out.println("column:"+column.getName());
            }
        }
    }
    public void dml(){
        DataSet set = new DataSet();
        service.execute("DELETE FROM HR_EMPLOYEE");
        set = service.querys("HR_EMPLOYEE");
        for(int i=0; i<3; i++){
            DataRow row = new DataRow();
            row.put("CODE","C"+i);
            row.put("NAME", "N"+i);
            set.add(row);
          //  service.insert("HR_EMPLOYEE", row);
        }
        service.insert("HR_EMPLOYEE", set);
        ConfigStore configs = new DefaultConfigStore();
        configs.and(Compare.LIKE, "NAME", "1");
        set = service.querys("HR_EMPLOYEE", configs);
        System.out.println(set);
    }

    public void ddl() throws Exception {
        //pg,ms,oracle,db2,ms2000,cms,dm8
        Table table = service.metadata().table("HR_EMPLOYEE");
        if (null != table) {
            log.warn("删除表:" + table.getName());
            service.ddl().drop(table);
        }
        table = service.metadata().table("HR_EMPLOYEE");

        //删除表
        Assertions.assertNull(table);


        //创建表
        createTable();

        table = service.metadata().table("HR_EMPLOYEE");
        if (null == table) {
            table = service.metadata().table("HR_EMPLOYEE");
        }
        Assertions.assertNotNull(table);

        //表备注
        Assertions.assertEquals(table.getComment(), "职员基础信息");

        //列备注
        Assertions.assertEquals(table.getColumn("NAME").getComment(), "姓名");

        //默认值 日期类型根据不同的数据库
        Object def = table.getColumn("DATA_STATUS").getDefaultValue();
        log.warn("默认值:{}", def);
        Assertions.assertNotNull(def);

        //主键
        PrimaryKey pk = table.getPrimaryKey();
        Assertions.assertNotNull(pk);
        Assertions.assertEquals(pk.getColumns().size(), 1);
        //Assertions.assertEquals(pk.getColumn("ID").getComment(), "主键");

        //索引(包含主键)
        Map<String, Index> indexes = table.getIndexs();
        //Assertions.assertEquals(indexes.size(), 2);
        //修改表结构
        table.setComment("新职员基础信息");
        table.addColumn("ALIAS", "varchar(10)").setComment("别名").setDefaultValue("def alias");
        table.addColumn("SLICE_COL", "varchar(10)").setComment("片段测试");
        table.getColumn("TMP").delete();
        table.getColumn("CODE").setType("varchar(300)").setComment("新备注");
        service.ddl().save(table);
        table = service.metadata().table("HR_EMPLOYEE");
        Assertions.assertEquals(table.getComment(), "新职员基础信息");
        Assertions.assertEquals(table.getColumn("ALIAS").getComment(), "别名");

        //修改列备注
        Column col = new Column("ALIAS");
        col.setTable("HR_EMPLOYEE");
        col.setComment("新别名");
        col.setType("varchar(255)");

        service.ddl().save(col);

        table = service.metadata().table("HR_EMPLOYEE");
        Assertions.assertEquals(table.getColumn("ALIAS").getComment(), "新别名");

        //修改列数据类型
        col = new Column("ALIAS");
        col.setTable("HR_EMPLOYEE");
        col.setType("varchar(20)");
        service.ddl().save(col);
        table = service.metadata().table("HR_EMPLOYEE");

        Assertions.assertEquals(table.getColumn("ALIAS").getPrecision(), 20);

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
        Table table = service.metadata().table(updateName, false);
        if(null != table){
            service.ddl().drop(table);
        }
        table = init(tableName);
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
                //service.ddl().drop(index);
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
        Assertions.assertEquals(updateComment, table.getComment());

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
        col = table.getColumn("CODE"); //oracle中会修改成varchar2
        //Assertions.assertEquals("VARCHAR(100)", col.getFullType().toUpperCase());

        //默认值 ABC::character varying 'ABC'
        Assertions.assertEquals("ABC", (col.getDefaultValue()+"").replace("'","").split("::")[0]);
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
    private void createTable() throws Exception{
        Table table = service.metadata().table("HR_EMPLOYEE");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("HR_EMPLOYEE").setComment("职员基础信息");
        //注意以下数据类型
        table.addColumn("ID"            , "BIGINT"       ).setComment("主键").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ).setComment("姓名")     ; // String          : nm
        table.addColumn("CODE"          , "varchar(50)"  ).setComment("工号")     ; // String          : workCode
        table.addColumn("BIRTHDAY"      , "DATE"         ).setComment("出生日期")  ; // Date            : birthday
        table.addColumn("JOIN_YMD"      , "varchar(10)"  ).setComment("入职日期")  ; // String          : joinYmd
        table.addColumn("SALARY"        , "float(10,2)"  ).setComment("工资")     ; // float           : salary
        table.addColumn("REMARK"        , "blob"         ).setComment("备注")     ; // byte[]          : remark
        table.addColumn("DATA_STATUS"   , "int"          ).setComment("状态").setDefaultValue(1);
        //注意SQL Server中一个表只能有一列TIMESTAMP， 不能设置默认值
        table.addColumn("CREATE_TIME"   , "DATETIME"    ).setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("UPDATE_TIME"   , "TIMESTAMP"   ).setComment("更新时间");
        table.addColumn("TMP"           , "INT"         );
        service.ddl().create(table);
        Index index = new Index();
        index.addColumn("NAME");
        index.addColumn("CODE");
        index.setTable(table);
        service.ddl().add(index);
    }
}
