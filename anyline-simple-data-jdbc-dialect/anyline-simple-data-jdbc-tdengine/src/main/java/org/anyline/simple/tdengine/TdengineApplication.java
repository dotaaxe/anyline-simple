package org.anyline.simple.tdengine;


import com.taosdata.jdbc.TSDBDriver;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.entity.PageNavi;
import org.anyline.metadata.*;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.anyline.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class TdengineApplication {
    private static Logger log = LoggerFactory.getLogger(TdengineApplication.class);
    private static AnylineService service;
    public static void main(String[] args) throws Exception{
        //test();
        SpringApplication application = new SpringApplication(TdengineApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = context.getBean(AnylineService.class);
        ConfigTable.IS_DDL_AUTO_DROP_COLUMN = true;

        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        try {
            clear();
            table();
            mtable();
            column();
            mcolumn();
            tag();
            dml();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void dml(){

        DataRow row = new DataRow();
        row.put("ID", System.currentTimeMillis());
        row.put("CODE","C");
        row.put("age", 10);

        service.insert("s_table_user_1", row);

        DataSet set = new DataSet();
        for(int i=0; i<10; i++){
            row = new DataRow();
            row.put("ID", System.currentTimeMillis()+i);
            row.put("CODE","C"+i);
            row.put("age", i*10);
            set.add(row);
        }


        log.warn(LogUtil.format("直接插入子表",34));
        service.insert("s_table_user_1", set);

        log.warn(LogUtil.format("通过超表向子表插入",34));
        set.tag("d","10");
        set.tag("i",1);
        set.tag("s","S1");
        //在这要根据主表+标签值查出子表,只有查出一个子表时才能执行，否则抛出异常
        //存在大量子表时，不要这样操作，定位子表需要时间，应该直接插入到子表中
        service.insert("s_table_user", set);


        long total = service.count("s_table_user");
        log.warn(LogUtil.format("统计数量:"+total,34));
        PageNavi navi = new DefaultPageNavi();
        navi.setTotalRow(total);
        navi.setCurPage(2);
        navi.setPageRows(3);
        set = service.querys("s_table_user", navi);
        log.warn(LogUtil.format("分页查询",34));

        System.out.println(set);
        set = service.querys("s_table_user");
        System.out.println(set);
        set = service.querys("s_table_user", "CODE:C0");
        System.out.println(set);
        set = service.querys("s_table_user", "CODE LIKE 'C0%'");
        System.out.println(set);

    }
    public static void clear() throws Exception{

        Table table = service.metadata().table("s_table_user");
        if(null != table) {
            log.warn(LogUtil.format("查看表Column(不含Tag)", 34));
            for (Column col : table.getColumns().values()) {
                log.warn(LogUtil.format(col.toString(), 34));
            }
            service.ddl().drop(new Table("s_table_user"));

        }
        MasterTable stable = service.metadata().mtable("s_table_user");
        log.warn(LogUtil.format("查看超表Column(不含Tag)",34));
        if(null != stable) {
            for (Column col : stable.getColumns().values()) {
                log.warn(LogUtil.format(col.toString(), 34));
            }
        }
        log.warn(LogUtil.format("查看超表TAG",34));
        if(null != stable) {
            for (Tag tag : stable.getTags().values()) {
                log.warn(LogUtil.format(tag.toString(), 34));
            }
        }
        service.ddl().drop(new MasterTable("s_table_user"));
    }
    public static void mcolumn() throws Exception{
        System.out.println("\n-------------------------------- start master table column  --------------------------------------------\n");

        LinkedHashMap<String, Column> columns = service.metadata().columns("s_table_user");
        log.warn(LogUtil.format("查看超表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        columns = service.metadata().columns("s_table_user_0");
        log.warn(LogUtil.format("查看壗子表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        Column column = new Column();
        column.setName("add_column");
        column.setTypeName("int");
        column.setTable("s_table_user");
        log.warn(LogUtil.format("超表添加Column:"+column.toString(),34));
        service.ddl().save(column);

        log.warn(LogUtil.format("超表修改column名:"+column.toString(),34));
        log.warn(LogUtil.format("超表不支持侯column名会抛出异常",31));
        try {
            column.update().setName("RENAME_COLUMN");
            service.ddl().save(column);
        }catch (Exception e){

        }

        log.warn(LogUtil.format("超表删除column:"+column.toString(),34));
        service.ddl().drop(column);
        System.out.println("\n-------------------------------- end master table column  --------------------------------------------\n");
    }
    public static void column() throws Exception{
        System.out.println("\n-------------------------------- start column  --------------------------------------------\n");
        LinkedHashMap<String, Column> columns = service.metadata().columns("s_table_user");
        log.warn(LogUtil.format("查看表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        Column column = new Column();
        column.setName("add_column");
        column.setTypeName("int");
        column.setTable("s_table_user");
        log.warn(LogUtil.format("表添加Column:"+column.toString(),34));
        service.ddl().save(column);

        log.warn(LogUtil.format("表修改column名:"+column.toString(),34));
        //超表不支持修改列表
        // column.update().setName("RENAME_COLUMN");
        // service.ddl().save(column);

        log.warn(LogUtil.format("表删除column:"+column.toString(),34));
        service.ddl().drop(column);
        System.out.println("\n-------------------------------- end column  --------------------------------------------\n");
    }
    public static void tag() throws Exception{
        System.out.println("\n-------------------------------- start tag  --------------------------------------------\n");

        LinkedHashMap<String, Tag> tags = service.metadata().tags("s_table_user");
        log.warn(LogUtil.format("查看超表TAG",34));
        for(Tag tag:tags.values()){
            log.warn(LogUtil.format(tag.toString(),34));
        }
        tags = service.metadata().tags("s_table_user_0");
        log.warn(LogUtil.format("查看子表TAG",34));
        for(Tag tag:tags.values()){
            log.warn(LogUtil.format(tag.toString(),34));
        }
        Tag tag = new Tag();
        tag.setName("add_tag");
        tag.setTypeName("int");
        tag.setTable("s_table_user");
        log.warn(LogUtil.format("超表添加tag:"+tag.toString(),34));
        service.ddl().save(tag);

        log.warn(LogUtil.format("超表修改tag名:"+tag.toString(),34));
        tag.update().setName("RENAME_TAG"+System.currentTimeMillis());
        service.ddl().save(tag);

        log.warn(LogUtil.format("超表删除tag:"+tag.toString(),34));
        service.ddl().drop(tag);
        System.out.println("\n-------------------------------- end tag  --------------------------------------------\n");
    }
    public static void mtable() throws Exception{
        System.out.println("\n-------------------------------- start stable  --------------------------------------------\n");

        LinkedHashMap<String,MasterTable> tables = service.metadata().mtables();
        log.warn(LogUtil.format("检索超表数量:"+tables.size(),34));
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }


        MasterTable table = service.metadata().mtable("s_table_user");
        if(null != table){
            //表存存 查看表结构
            log.warn(LogUtil.format("查询表结构:"+table.getName(),34));
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",34));
            service.ddl().drop(table);
        }else{
            //表不存在  创建新表
            table = new MasterTable("s_table_user");

        }
        //table.setComment("表备注");
        table.addColumn("ID", "TIMESTAMP");
        table.addColumn("CODE","NCHAR(10)").setComment("编号");

        table.addTag("I", "int").setComment("int tag");
        table.addTag("D", "double");
        table.addTag("S", "nchar(10)");
        table.setName("s_table_user");

        log.warn(LogUtil.format("创建超表",34));
        service.ddl().save(table);

        table.addColumn("age","int");
        table.addColumn("NAME","NCHAR(10)");
        table.addTag("NT", "int");
        log.warn(LogUtil.format("超表添加列与标签",34));
        service.ddl().save(table);
        //创建子表
        for(int i=0;i < 10;i++){
            PartitionTable item = new PartitionTable();
            item.setName("s_table_user_"+i);
            item.setMasterName("s_table_user");
            item.addTag("I", null,i);
            item.addTag("D", null, 10);
            item.addTag("S", null, "S"+i);
            log.warn(LogUtil.format("删除子表",34));
            service.ddl().drop(item);
            log.warn(LogUtil.format("创建子表",34));
            service.ddl().create(item);
        }

        log.warn(LogUtil.format("根据主表查询子表",34));
        LinkedHashMap<String,PartitionTable> items = service.metadata().ptables(table);
        for(Table item:items.values()){
            log.warn("子表:"+item.getName());
        }
        log.warn(LogUtil.format("根据主表与标签值查询子表",34));
        Map<String,Object> tags = new HashMap<>();
        tags.put("I", 1);
        tags.put("D",10);
        tags.put("S", "S1");
        items = service.metadata().ptables(table, tags);
        for(Table item:items.values()){
            log.warn("子表:"+item.getName());
        }
        System.out.println("\n-------------------------------- end stable  --------------------------------------------\n");
    }
    public static void table() throws Exception{
        System.out.println("\n-------------------------------- start table  --------------------------------------------\n");

        LinkedHashMap<String,Table> tables = service.metadata().tables();
        log.warn(LogUtil.format("检索表数量:"+tables.size(),34));
        int qty = 0;
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
            if(qty++ > 10){
                break;
            }
        }


        Table table = service.metadata().table("s_table_user");
        if(null != table){
            service.ddl().drop(table);
        }

        //数据类型参考这里 https://docs.taosdata.com/taos-sql/data-type/
        log.warn(LogUtil.format("创建表,第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键",34));
        table = new Table("s_table_user");
        //第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键
        table.addColumn("ID", "TIMESTAMP");
        //用NCHAR  不要用 VARCHAR(BINARY类型的别名)
        table.addColumn("CODE","NCHAR(10)").setComment("编号");
        table.addColumn("age","int");
        service.ddl().save(table);

        table = service.metadata().table("s_table_user");
        if(null != table){
            log.warn(LogUtil.format("查询表结构:"+table.getName(),34));
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",34));
            service.ddl().drop(table);
        }

        System.out.println("\n-------------------------------- end table  --------------------------------------------\n");
    }
    public static void test() throws Exception{

        Statement stmt = getConn().createStatement();

        //这样可以，只能查全表，不能加任何条件
        //ResultSet resultSet = stmt.executeQuery("select * from s_table_user");
        //这样就异常了
        ResultSet resultSet = stmt.executeQuery("select * from s_table_user limit 1,2");

        Timestamp ts = null;
        int temperature = 0;
        String humidity = "";
        while(resultSet.next()){

            ts = resultSet.getTimestamp(1);
            temperature = resultSet.getInt(3);
            humidity = resultSet.getString("code");

            System.out.printf("%s, %d, %s\n", ts, temperature, humidity);
        }
    }
    public static  Connection getConn() throws Exception{
        Class.forName("com.taosdata.jdbc.TSDBDriver");
        String jdbcUrl = "jdbc:TAOS://localhost:6030/simple?user=root&password=root";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        connProps.setProperty("debugFlag", "135");
        connProps.setProperty("maxSQLLength", "1048576");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        return conn;
    }
}
